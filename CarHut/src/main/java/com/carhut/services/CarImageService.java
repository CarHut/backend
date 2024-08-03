package com.carhut.services;

import com.carhut.database.repository.CarImageRepository;
import com.carhut.database.repository.ColorRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.CarImage;
import com.carhut.requests.PrincipalRequest;
import com.carhut.security.annotations.UserAccessCheck;
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
import java.util.UUID;

@Service
public class CarImageService {

    @Autowired
    private CarImageRepository carImageRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private ColorRepository colorRepository;

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

    @UserAccessCheck
    public void addImagesToDatabase(PrincipalRequest<CarHutCar> carHutCar1, List<MultipartFile> multipartFiles) throws Exception {
        if (carHutCar1 == null) {
            return;
        }

        if (multipartFiles == null || carHutCar1.getDto() == null) {
            System.out.println("Cannot add images to database because carHutCar or multipartFiles is null.");
            return;
        }

        if (multipartFiles.isEmpty()) {
            System.out.println("List of images is empty. Nothing will be added to database.");
            return;
        }

        CarHutCar carHutCar = carHutCar1.getDto();
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

        String locationPath = basePath + newCar.getId();
        Path path = Files.createDirectory(Path.of(locationPath));

        for (MultipartFile image : multipartFiles) {
            if (image != null) {
                String postfix = switch (image.getContentType()) {
                    case "image/jpg" -> ".jpg";
                    case "image/jpeg" -> ".jpeg";
                    default -> ".png";
                };

                String id = generateNewImageId(image);
                File file = new File(path + "/" + id + postfix);
                CarImage carImage = new CarImage(id, file.getPath(), carHutCar.getId(), newCar.getSellerId(), true);
                image.transferTo(file);

                carImageRepository.save(carImage);
            }
        }
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
