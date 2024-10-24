package com.carhut.proxy.processor;

import com.carhut.proxy.builder.URLBuilder;
import com.carhut.proxy.cache.CacheState;
import com.carhut.proxy.cache.RedisCacheHandler;
import com.carhut.proxy.dispatcher.WorkerThreadDispatcher;
import com.carhut.proxy.model.RequestModel;
import com.carhut.proxy.model.ResponseModel;
import com.carhut.proxy.model.UnifiedRESTResponseModel;
import com.carhut.proxy.util.logger.ProxyLogger;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class RequestTaskProcessor implements TaskProcessor<RequestModel, CompletableFuture<ResponseModel>> {

    private static final ProxyLogger logger = ProxyLogger.getInstance();
    private final RedisCacheHandler redisCacheHandler = new RedisCacheHandler();

    public RequestTaskProcessor() {}

    @Override
    public CompletableFuture<ResponseModel> processRequest(RequestModel request) {
        CacheState cacheState = redisCacheHandler.cacheAvailability(request);

        if (cacheState == CacheState.CACHE_AVAILABLE) {
            CompletableFuture<ResponseModel> cf = new CompletableFuture<>();
            cf.complete(redisCacheHandler.getCache(request));
            return cf;
        }

        if (cacheState == CacheState.REQUEST_ADDED_TO_CACHE_PROVIDE_RESPONSE) {
            Function<Void, CompletableFuture<ResponseModel>> function = buildFunctionToExecuteAndAddCache(request);
            return WorkerThreadDispatcher.getInstance().dispatchThread(function);
        }

        Function<Void, CompletableFuture<ResponseModel>> function = buildFunctionToExecute(request);
        return WorkerThreadDispatcher.getInstance().dispatchThread(function);
    }

    public Function<Void, CompletableFuture<ResponseModel>> buildFunctionToExecuteAndAddCache(RequestModel input) {
        return (unused) -> {
            HttpRequest httpRequest = buildRequest(input);

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20)) // Request delay
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        // Build a ResponseModel from the HttpResponse
                        int statusCode = httpResponse.statusCode();
                        String responseBody = httpResponse.body();
                        if (statusCode >= 200 && statusCode < 300) {
                            ResponseModel model = new UnifiedRESTResponseModel(responseBody, statusCode, "Success");
                            redisCacheHandler.addCache(input, model);
                            return model;
                        } else {
                            redisCacheHandler.addCache(input, null);
                            return new UnifiedRESTResponseModel(responseBody, statusCode, "Error");
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return new UnifiedRESTResponseModel("Failed to complete request", 500, "Failure");
                    });
        };
    }

    @Override
    public Function<Void, CompletableFuture<ResponseModel>> buildFunctionToExecute(RequestModel input) {
        return (unused) -> {
            HttpRequest httpRequest = buildRequest(input);

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20)) // Request delay
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(httpResponse -> {
                        // Build a ResponseModel from the HttpResponse
                        int statusCode = httpResponse.statusCode();
                        String responseBody = httpResponse.body();

                        if (statusCode >= 200 && statusCode < 300) {
                            return (ResponseModel) new UnifiedRESTResponseModel(responseBody, statusCode, "Success");
                        } else {
                            return new UnifiedRESTResponseModel(responseBody, statusCode, "Error");
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return new UnifiedRESTResponseModel("Failed to complete request", 500, "Failure");
                    });
        };
    }

    private HttpRequest buildRequest(RequestModel request) {
        try {
            URI uri = URLBuilder.buildRequestUrl(request.getRequest());

            HttpRequest.Builder httpRequest = HttpRequest.newBuilder();
            httpRequest.uri(uri);

            if (request.getBearerToken() != null) {
                httpRequest.header("Authorization", request.getBearerToken());
            }

            switch (request.getRequest().getMethod()) {
                case "GET":
                    httpRequest.GET();
                    break;
                case "POST":
                    if (request.getMultipartFiles() != null && request.getMultipartJson() != null) {
                        String boundary = "-------------------------WebKitFormBoundary7MA4YWxkTrZu0gW";
                        httpRequest.header("Content-Type", "multipart/form-data; boundary=" + boundary);
                        httpRequest.POST(buildMultipartBody(request, boundary));
                    } else if (request.getBody() != null && !request.getBody().isBlank()) {
                        httpRequest.header("Content-Type",
                                request.getContentType() == null ? "application/json" : request.getContentType());
                        httpRequest.POST(HttpRequest.BodyPublishers.ofString(request.getBody()));
                    } else {
                        return null;
                    }
                    break;
                default:
                    return null;
            }

            httpRequest.timeout(Duration.ofSeconds(20));

            return httpRequest.build();

        } catch (Exception e) {
            logger.logWarn("Exception occurred while trying to build request. Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private HttpRequest.BodyPublisher buildMultipartBody(RequestModel request, String boundary) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            outputStream.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.ISO_8859_1));
            outputStream.write(("Content-Disposition: form-data; name=\"json\"\r\n").getBytes(StandardCharsets.ISO_8859_1));
            outputStream.write(("Content-Type: application/json\r\n\r\n").getBytes(StandardCharsets.ISO_8859_1));
            outputStream.write(request.getMultipartJson().getBytes(StandardCharsets.ISO_8859_1));
            outputStream.write("\r\n".getBytes(StandardCharsets.ISO_8859_1));

            List<MultipartFile> files = request.getMultipartFiles();
            if (files != null) {
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    outputStream.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.ISO_8859_1));
                    outputStream.write(("Content-Disposition: form-data; name=\"files\"; filename=\"" + fileName + "\"\r\n").getBytes(StandardCharsets.ISO_8859_1));
                    outputStream.write(("Content-Type: " + file.getContentType() + "\r\n\r\n").getBytes(StandardCharsets.ISO_8859_1));
                    outputStream.write(file.getBytes());
                    outputStream.write("\r\n".getBytes(StandardCharsets.ISO_8859_1));
                }
            }

            outputStream.write(("--" + boundary + "--\r\n").getBytes(StandardCharsets.ISO_8859_1));
            return HttpRequest.BodyPublishers.ofByteArray(outputStream.toByteArray());
        } catch (IOException e) {
            logger.logWarn("Exception occurred while trying to build request. Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
