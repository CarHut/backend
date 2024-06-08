package com.carhut.services;

import com.carhut.database.repository.CarImageRepository;
import com.carhut.database.repository.ColorRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.CarImage;
import com.carhut.models.carhut.UploadCarModel;
import com.carhut.paths.Paths;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

@Service
public class CarImageService {

    @Autowired
    private CarImageRepository carImageRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private ColorRepository colorRepository;

    public List<byte[]> getImagesWithCarId(String carId) throws IOException {
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

    public void addImagesToDatabase(CarHutCar carHutCar, List<MultipartFile> multipartFiles) throws Exception {
        CarHutCar newCar = new CarHutCar(userCredentialsRepository.findUserIdByUsername(carHutCar.getSellerId()), carHutCar.getSellerAddress(),
                carHutCar.getBrandId(), carHutCar.getModelId(), carHutCar.getHeader(),
                carHutCar.getPrice(), carHutCar.getMileage(), carHutCar.getRegistration(),
                carHutCar.getEnginePower(), carHutCar.getEngineDisplacement(), carHutCar.getFuel(),
                carHutCar.getFuelConsumptionAvg(), carHutCar.getFuelConsumptionCity(), carHutCar.getFuelConsumptionHighway(),
                carHutCar.getGearbox(), carHutCar.getGearboxGears(), carHutCar.getBodyType(),
                carHutCar.getPowertrain(), carHutCar.getDescription(), carHutCar.getBaseImgPath(),
                carHutCar.getPreviousOwners(), carHutCar.getEnergyEffClass(), carHutCar.getSeats(),
                carHutCar.getDoors(), carHutCar.getEmissionClass(), colorRepository.getColorIdByColorName(carHutCar.getExteriorColorId()),
                colorRepository.getColorIdByColorName(carHutCar.getInteriorColorId()), carHutCar.getDamageStatus(),
                carHutCar.isParkingSensors(), carHutCar.isParkingCameras(), carHutCar.getCountryOfOrigin(),
                carHutCar.getTechnicalInspectionDate(), carHutCar.getEmissionInspectionDate(), carHutCar.getFeatures(), null);

        String basePath = System.getProperty("user.dir");
        String locationPath = Paths.carHutImages + "/" + newCar.getId();

        Path path = Files.createDirectory(Path.of(basePath + locationPath));

        for (MultipartFile image : multipartFiles) {
            String postfix = switch (image.getContentType()) {
                case "image/jpg" -> ".jpg";
                case "image/jpeg" -> ".jpeg";
                case null -> null;
                default -> ".png";
            };

            String id = generateNewImageId(image);
            File file = new File(path + "/" + id + postfix);
            CarImage carImage = new CarImage(id, file.getPath(), newCar.getId(), newCar.getSellerId(), true);
            image.transferTo(file);
            carImageRepository.save(carImage);
        }
    }

    private String generateNewImageId(MultipartFile image) throws NoSuchAlgorithmException {
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

    public void uploadImageToFileSystem(MultipartFile image, String username) throws IOException {
        String basePath = System.getProperty("user.dir");
        File uploadDir = new File(basePath + Paths.carHutImages + username);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String postfix = switch (image.getContentType()) {
            case "image/jpg" -> ".jpg";
            case "image/jpeg" -> ".jpeg";
            case null -> null;
            default -> ".png";
        };

        File file = new File(basePath + Paths.carHutImages + username + "/temp_" + UUID.randomUUID() + postfix);

        image.transferTo(file);
    }
}
