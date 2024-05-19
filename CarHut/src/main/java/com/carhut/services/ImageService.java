package com.carhut.services;

import com.carhut.database.repository.ColorRepository;
import com.carhut.database.repository.ImageRepository;
import com.carhut.database.repository.UserCredentialsRepository;
import com.carhut.models.carhut.CarHutCar;
import com.carhut.models.carhut.Image;
import com.carhut.paths.Paths;
import com.sun.tools.jconsole.JConsoleContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserCredentialsRepository userCredentialsRepository;
    @Autowired
    private ColorRepository colorRepository;

    @Deprecated
    public Image getImageById(String id) {
        return imageRepository.getImageById(id);
    }

    @Deprecated
    public List<Image> getImagesByCarId(String carId) {
        return imageRepository.getImagesByCarId(carId);
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

    public void addImagesToDatabase(CarHutCar carHutCar) throws Exception {
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
        String locationPath = Paths.carHutImages + carHutCar.getSellerId() + "/" + newCar.getId();

        Path path = Files.createDirectory(Path.of(basePath + locationPath));
        Path formerPath = java.nio.file.Paths.get( basePath + Paths.carHutImages + carHutCar.getSellerId());

        try (Stream<Path> files = Files.walk(formerPath)) {
            files.filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Path relativePath = formerPath.relativize(file);
                            Path targetPath = path.resolve(relativePath);
                            Files.createDirectories(targetPath.getParent());
                            Files.copy(file, targetPath);
                            Files.delete(file);
                        } catch (IOException e) {
                            throw new RuntimeException("Error copying or deleting file: " + file, e);
                        }
                    });
        }
    }


    public List<byte[]> getImagesWithCarId(String carId) throws IOException {
        List<byte[]> resources = new ArrayList<>();
        String sellerId = userCredentialsRepository.findUserUsernameByCarId(carId);

        String basePath = System.getProperty("user.dir");

        Path formerPath = java.nio.file.Paths.get( basePath + Paths.carHutImages + sellerId + "/" + carId);
        File directory = new File(String.valueOf(formerPath));
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    byte[] imageResource = convertToResource(file);
                    if (imageResource != null) {
                        resources.add(imageResource);
                    }
                }
            }
        }

        return resources;
    }

    public byte[] convertToResource(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

}
