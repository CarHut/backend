package imageservice.controllers;

import imageservice.dtos.request.ImagesDto;
import imageservice.services.CarImageService;
import imageservice.status.ImageServiceError;
import imageservice.status.ImageServiceResultInterface;
import imageservice.utils.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(path = "/api/carhut")
public class CarImageController {

    @Autowired
    private CarImageService carImageService;
    private ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping(value = "/getImages")
    @ResponseBody
    public ResponseEntity<List<Byte[]>> getImages(@RequestParam String carId) {
        List<Byte[]> images = null;

        try {
            images = carImageService.getImagesWithCarId(carId).get();
            controllerLogger.saveToFile("[CarHutAPIController][OK]: /getImages successfully executed.");
        } catch (Exception e) {
            controllerLogger.saveToFile("[CarHutAPIController][ERROR]: /getColorStringNameFromColorId something went wrong. Message: " + e.getMessage());
        }

        if (images != null) {
            return ResponseEntity.ok(images);
        } else {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping(value = "/addImagesToDatabase")
    public ResponseEntity<String> addImagesToDatabase(@RequestBody ImagesDto imagesDto)
            throws ExecutionException, InterruptedException {
        ImageServiceResultInterface imageStatus = carImageService.addImagesToDatabase(imagesDto).get();
        if (imageStatus instanceof ImageServiceError) {
            return ResponseEntity.internalServerError().body("Cannot add images to database. Please try again later.");
        }

        return ResponseEntity.ok("Successfully added images to database.");
    }

}
