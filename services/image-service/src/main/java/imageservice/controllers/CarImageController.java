package imageservice.controllers;

import imageservice.dtos.request.ImagesDto;
import imageservice.dtos.response.ImageStatusResponse;
import imageservice.services.CarImageService;
import imageservice.status.ImageServiceErrors;
import imageservice.status.ImageServiceStatusInterface;
import imageservice.utils.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/api/carhut")
public class CarImageController {

    @Autowired
    private CarImageService carImageService;
    private ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping(value = "/getImages")
    @ResponseBody
    public ImageStatusResponse<List<byte[]>> getImages(@RequestParam String carId) {
        List<byte[]> images = null;

        try {
            images = carImageService.getImagesWithCarId(carId);
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getImages successfully executed.");
        } catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColorStringNameFromColorId something went wrong. Message: " + e.getMessage());
        }

        if (images != null) {
            return new ImageStatusResponse<>("SUCCESS",
                    200,
                    "Messages successfully fetched for carId: " + carId + ".",
                    images);
        } else {
            return new ImageStatusResponse<>("ERROR",
                    500,
                    "Cannot fetch images for carId: " + carId + ".",
                    null);
        }
    }

    @PostMapping(value = "/addImagesToDatabase")
    public ImageStatusResponse<String> addImagesToDatabase(@RequestBody ImagesDto imagesDto) {
        ImageServiceStatusInterface imageStatus = carImageService.addImagesToDatabase(imagesDto);
        if (imageStatus instanceof ImageServiceErrors) {
            return new ImageStatusResponse<>(imageStatus.toString(),
                    201,
                    "Cannot add images to to database.",
                    null);
        }

        return new ImageStatusResponse<>(imageStatus.toString(),
                500,
                "Successfully added images to database.",
                null);

    }

}
