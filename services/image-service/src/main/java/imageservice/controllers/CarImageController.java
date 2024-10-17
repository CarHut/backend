package imageservice.controllers;

import imageservice.services.CarImageService;
import imageservice.status.ImageServiceError;
import imageservice.status.ImageServiceResultInterface;
import imageservice.utils.ControllerLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping(path = "/image-service")
public class CarImageController {

    @Autowired
    private CarImageService carImageService;
    private ControllerLogger controllerLogger = ControllerLogger.getLogger();

    @GetMapping(value = "/get-images")
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

    @PostMapping(value = "/add-images-to-database", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ResponseEntity<String> addImagesToDatabase(@RequestParam("seller-id") String sellerId,
                                                      @RequestParam("car-id") String carId,
                                                      @RequestPart(value = "images") List<MultipartFile> images)
            throws ExecutionException, InterruptedException {
        ImageServiceResultInterface imageStatus = carImageService.addImagesToDatabase(sellerId, carId, images).get();
        if (imageStatus instanceof ImageServiceError) {
            return ResponseEntity.internalServerError().body("Cannot add images to database. Please try again later.");
        }

        return ResponseEntity.status(201).body("Successfully added images to database.");
    }

}
