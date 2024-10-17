package com.carhut.http;

import com.carhut.commons.model.CarHutCar;
import com.carhut.enums.ServiceStatusEntity;
import org.springframework.web.multipart.MultipartFile;

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

    private final String basePath = "http://localhost:8051/image-service";

    public CompletableFuture<HttpResponse<String>> saveImages(CarHutCar car, List<MultipartFile> images)
            throws IOException, URISyntaxException {
        return sendRequestToSaveImages(car, images);
    }

    private CompletableFuture<HttpResponse<String>> sendRequestToSaveImages(CarHutCar car, List<MultipartFile> images)
            throws IOException, URISyntaxException {
        String boundary = "Boundary-" + System.currentTimeMillis();
        StringBuilder multipartBody = new StringBuilder();

        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            multipartBody.append("--").append(boundary).append("\r\n");
            multipartBody.append("Content-Disposition: form-data; name=\"images\"; filename=\"").append(fileName).append("\"\r\n");
            multipartBody.append("Content-Type: ").append(image.getContentType()).append("\r\n\r\n");

            // Append image data as raw bytes
            multipartBody.append(new String(image.getBytes(), StandardCharsets.ISO_8859_1)).append("\r\n");
        }
        multipartBody.append("--").append(boundary).append("--\r\n");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(basePath + "/add-images-to-database?seller-id=" + car.getSellerId()
                        + "&car-id=" + car.getId()))
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .timeout(Duration.ofSeconds(30))  // Increase timeout if necessary
                .POST(HttpRequest.BodyPublishers.ofString(multipartBody.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

}
