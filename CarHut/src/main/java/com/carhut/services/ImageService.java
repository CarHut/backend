package com.carhut.services;

import com.carhut.database.repository.ImageRepository;
import com.carhut.models.carhut.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public void uploadImage(MultipartFile image, String username) {

    }
}
