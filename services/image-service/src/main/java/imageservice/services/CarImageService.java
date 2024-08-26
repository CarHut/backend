package imageservice.services;

import imageservice.dtos.request.ImagesDto;
import imageservice.models.CarImage;
import imageservice.repositories.CarImageRepository;
import imageservice.status.ImageServiceErrors;
import imageservice.status.ImageServiceStatus;
import imageservice.status.ImageServiceStatusInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CarImageService {

    @Autowired
    private CarImageRepository carImageRepository;

    @Value("${images.path}")
    private String basePath;

    public List<byte[]> getImagesWithCarId(String carId) throws IOException {
        if (carId == null || carId.isEmpty()) {
            return null;
        }

        List<byte[]> resources = new ArrayList<>();
        List<CarImage> images = carImageRepository.getImagesByCarId(carId);

        for (CarImage image : images) {    
            File file = new File(image.getPath());
            if (file.isFile()) {
                byte[] imageResource = convertToResource(file);
                if (imageResource != null) {
                    resources.add(imageResource);
                }
            }
        }

        return resources;
    }

    public byte[] convertToResource(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public ImageServiceStatusInterface addImagesToDatabase(ImagesDto imagesDto) {
        if (imagesDto == null) {
            return ImageServiceErrors.ERROR_OBJECT_IS_NULL;
        }

        if (imagesDto.carHutCarInfoDto() == null) {
            return ImageServiceErrors.ERROR_OBJECT_IS_NULL;
        }

        if (imagesDto.carHutCarInfoDto().newCarId() == null || imagesDto.carHutCarInfoDto().sellerId() == null) {
            return ImageServiceErrors.ERROR_OBJECT_IS_NULL;
        }

        if (imagesDto.multipartFiles() == null) {
            System.out.println("Cannot add images to database because multipartFiles is null.");
            return ImageServiceErrors.ERROR_OBJECT_IS_NULL;
        }

        if (imagesDto.multipartFiles().isEmpty()) {
            System.out.println("List of images is empty. Nothing will be added to database.");
            return ImageServiceErrors.IMAGE_LIST_IS_EMPTY;
        }

        Path path = createRepositoryForNewCarImages(imagesDto);
        if (path == null) {
            return ImageServiceErrors.ERROR_CREATING_DIRECTORY;
        }

        ImageServiceErrors errorUploadingImageToFileSystem = saveImages(imagesDto, path);
        return Objects.requireNonNullElse(errorUploadingImageToFileSystem, ImageServiceStatus.SUCCESS);
    }

    private Path createRepositoryForNewCarImages(ImagesDto imagesDto) {
        String locationPath = basePath + imagesDto.carHutCarInfoDto().newCarId();
        Path path = null;
        try {
            path = Files.createDirectory(Path.of(locationPath));
        } catch (IOException e) {
            return null;
        }
        return path;
    }

    private ImageServiceErrors saveImages(ImagesDto imagesDto, Path path) {
        for (MultipartFile image : imagesDto.multipartFiles()) {
            if (image != null) {
                CarImage carImage = saveImageToFileSystem(imagesDto, path, image);
                if (carImage == null) {
                    // Create fallback to remove existing images from database
                    return ImageServiceErrors.ERROR_UPLOADING_IMAGE_TO_FILE_SYSTEM;
                }
                carImageRepository.save(carImage);
            }
        }
        return null;
    }

    private CarImage saveImageToFileSystem(ImagesDto imagesDto, Path path, MultipartFile image) {
        String postfix = switch (image.getContentType()) {
            case "image/jpg" -> ".jpg";
            case "image/jpeg" -> ".jpeg";
            default -> ".png";
        };

        String id = generateNewImageId(image);
        File file = new File(path + "/" + id + postfix);
        CarImage carImage = new CarImage(id, file.getPath(), imagesDto.carHutCarInfoDto().newCarId(), imagesDto.carHutCarInfoDto().sellerId(), true);
        try {
            image.transferTo(file);
        } catch (IOException e) {
            return null;
        }
        return carImage;
    }

    private String generateNewImageId(MultipartFile image) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(image.getBytes());

            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }

            return hexStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
        For testing only
     */
    public void deleteAll() {
        carImageRepository.deleteAll();
    }
}
