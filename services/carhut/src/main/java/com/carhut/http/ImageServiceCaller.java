package com.carhut.http;

import com.carhut.commons.model.CarHutCar;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ImageServiceCaller {

    private final String basePath = "http://localhost:8011/image-service";

    public CompletableFuture<HttpResponse<String>> saveImages(CarHutCar car, List<MultipartFile> images)
            throws IOException, URISyntaxException {
        return sendRequestToSaveImages(car, images);
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToSaveImages(CarHutCar car, List<MultipartFile> images)
            throws IOException, URISyntaxException {
        String boundary = "Boundary-" + System.currentTimeMillis();

        // Use the refactored prepareMultipartBody method, which now returns a byte array
        byte[] multipartBody = prepareMultipartBody(images, boundary);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/add-images-to-database?seller-id=" + car.getSellerId()
                        + "&car-id=" + car.getId()))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .timeout(Duration.ofSeconds(15))
                // Use ofByteArray to send the binary data in the request body
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private byte[] prepareMultipartBody(List<MultipartFile> images, String boundary) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            outputStream.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.ISO_8859_1));
            outputStream.write(("Content-Disposition: form-data; name=\"images\"; filename=\"" + fileName + "\"\r\n").getBytes(StandardCharsets.ISO_8859_1));
            outputStream.write(("Content-Type: " + image.getContentType() + "\r\n\r\n").getBytes(StandardCharsets.ISO_8859_1));

            outputStream.write(image.getBytes());
            outputStream.write("\r\n".getBytes(StandardCharsets.ISO_8859_1));
        }
        outputStream.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.ISO_8859_1));

        return outputStream.toByteArray();
    }

}
