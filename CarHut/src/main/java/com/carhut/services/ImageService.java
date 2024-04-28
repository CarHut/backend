package com.carhut.services;

import com.carhut.database.repository.ImageRepository;
import com.carhut.models.carhut.Image;
import com.carhut.secret.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image getImageById(String id) {
        return imageRepository.getImageById(id);
    }

    public List<Image> getImagesByCarId(String carId) {
        return imageRepository.getImagesByCarId(carId);
    }

    public void uploadImageToFileSystem(MultipartFile image, String username) throws IOException {
        File uploadDir = new File(Paths.carHutImages + username);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String postfix = switch (image.getContentType()) {
            case "image/jpg" -> ".jpg";
            case "image/jpeg" -> ".jpeg";
            case null -> null;
            default -> ".png";
        };

        File file = new File(Paths.carHutImages + username + "\\temp_" + UUID.randomUUID() + postfix);

        image.transferTo(file);
    }
}
