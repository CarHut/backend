package imageservice.controller;

import imageservice.service.CarImageService;
import imageservice.status.ImageServiceError;
import imageservice.status.ImageServiceResultInterface;
import imageservice.util.logger.ImageServiceLogger;
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
    private ImageServiceLogger logger = ImageServiceLogger.getInstance();

    @GetMapping(value = "/get-images")
    @ResponseBody
    public ResponseEntity<List<Byte[]>> getImages(@RequestParam("car-id") String carId) {
        List<Byte[]> images = null;

        try {
            images = carImageService.getImagesWithCarId(carId).get();
            logger.logInfo("[CarHutAPIController][OK]: /getImages successfully executed.");
        } catch (Exception e) {
            logger.logError("[CarHutAPIController][ERROR]: /getColorStringNameFromColorId something went wrong. Message: " + e.getMessage());
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
            logger.logError("[CarHutAPIController][ERROR]: /add-images-to-database something went wrong.");
            return ResponseEntity.internalServerError().body("Cannot add images to database. Please try again later.");
        }
        logger.logInfo("[CarHutAPIController][OK]: /add-images-to-database successfully executed.");
        return ResponseEntity.status(201).body("Successfully added images to database.");
    }

}
