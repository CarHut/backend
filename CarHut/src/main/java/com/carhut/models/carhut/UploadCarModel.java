package com.carhut.models.carhut;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UploadCarModel {

    private CarHutCar carHutCar;
    private List<MultipartFile> multipartFiles;

    public UploadCarModel() {}

    public UploadCarModel(CarHutCar carHutCar, List<MultipartFile> multipartFiles) {
        this.carHutCar = carHutCar;
        this.multipartFiles = multipartFiles;
    }
}
