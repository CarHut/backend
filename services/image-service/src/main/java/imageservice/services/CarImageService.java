package imageservice.services;

import imageservice.models.CarImage;
import imageservice.repositories.CarImageRepository;
import imageservice.repositories.resourceprovider.ImageDatabaseResourceManager;
import imageservice.status.ImageServiceError;
import imageservice.status.ImageServiceResultInterface;
import imageservice.status.ImageServiceStatus;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Service
public class CarImageService {

    @Autowired
    private CarImageRepository carImageRepository;
    private ImageDatabaseResourceManager imageDatabaseResourceManager = ImageDatabaseResourceManager.getInstance();

    @Value("${images.path}")
    private String basePath;

    public CompletableFuture<List<Byte[]>> getImagesWithCarId(String carId) throws IOException {
        CompletableFuture<List<Byte[]>> cf = new CompletableFuture<>();
        if (carId == null || carId.isEmpty()) {
            cf.complete(null);
            return cf;
        }

        Function<Void, List<Byte[]>> function = (unused) -> {
            try {
                List<Byte[]> resources = new ArrayList<>();
                List<CarImage> images = carImageRepository.getImagesByCarId(carId);
                for (CarImage image : images) {
                    File file = new File(image.getPath());
                    if (file.isFile()) {
                        byte[] imageResource = convertToResource(file);
                        if (imageResource != null) {
                            // Convert byte[] to Byte[]
                            Byte[] imageResourceBoxed = new Byte[imageResource.length];
                            for (int i = 0; i < imageResource.length; i++) {
                                imageResourceBoxed[i] = imageResource[i];
                            }
                            resources.add(imageResourceBoxed);
                        }
                    }
                }

                return resources;
            } catch (Exception e) {
                return null;
            }
        };

        return imageDatabaseResourceManager.acquireDatabaseResource(function);
    }

    public byte[] convertToResource(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public CompletableFuture<ImageServiceResultInterface> addImagesToDatabase(String sellerId, String carId,
                                                                              List<MultipartFile> images)
            throws ExecutionException, InterruptedException {
        CompletableFuture<ImageServiceResultInterface> cf = new CompletableFuture<>();
        if (images == null) {
            cf.complete(ImageServiceError.ERROR_OBJECT_IS_NULL);
            return cf;
        }

        if (sellerId == null) {
            cf.complete(ImageServiceError.ERROR_OBJECT_IS_NULL);
            return cf;
        }

        if (carId == null) {
            cf.complete(ImageServiceError.ERROR_OBJECT_IS_NULL);
            return cf;
        }


        if (images.isEmpty()) {
            System.out.println("List of images is empty. Nothing will be added to database.");
            cf.complete(ImageServiceError.IMAGE_LIST_IS_EMPTY);
            return cf;
        }

        Path path = createRepositoryForNewCarImages(carId);
        if (path == null) {
            cf.complete(ImageServiceError.ERROR_CREATING_DIRECTORY);
            return cf;
        }

        ImageServiceError errorUploadingImageToFileSystem = saveImages(sellerId, carId, images, path).get();
        cf.complete(errorUploadingImageToFileSystem != null
                ? errorUploadingImageToFileSystem
                : ImageServiceStatus.SUCCESS);
        return cf;
    }

    private Path createRepositoryForNewCarImages(String carId) {
        String locationPath = basePath + "/" + carId;
        Path path = null;
        try {
            path = Files.createDirectory(Path.of(locationPath));
        } catch (IOException e) {
            return null;
        }
        return path;
    }

    private CompletableFuture<ImageServiceError> saveImages(String sellerId, String carId,
                                                            List<MultipartFile> images, Path path) {
        Function<Void, ImageServiceError> function = (unused) -> {
            for (MultipartFile image : images) {
                if (image != null) {
                    CarImage carImage = saveImageToFileSystem(sellerId, carId, path, image);
                    if (carImage == null) {
                        // Create fallback to remove existing images from database
                        return ImageServiceError.ERROR_UPLOADING_IMAGE_TO_FILE_SYSTEM;
                    }
                    try {
                        carImageRepository.save(carImage);
                    } catch (Exception e) {
                        return ImageServiceError.ERROR_UPLOADING_IMAGE_TO_DATABASE;
                    }
                }
            }
            return null;
        };

        return imageDatabaseResourceManager.acquireDatabaseResource(function);
    }

    private CarImage saveImageToFileSystem(String sellerId, String carId, Path path, MultipartFile image) {
        String postfix = switch (image.getContentType()) {
            case "image/jpg" -> ".jpg";
            case "image/jpeg" -> ".jpeg";
            default -> ".png";
        };

        String id = generateNewImageId(image);
        File file = new File(path + "/" + id + postfix);
        CarImage carImage = new CarImage(id, file.getPath(), carId, sellerId, true);
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
