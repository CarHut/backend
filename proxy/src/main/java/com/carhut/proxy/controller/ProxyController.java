package com.carhut.proxy.controller;

import com.carhut.proxy.controller.ratelimit.RateLimitingProtection;
import com.carhut.proxy.model.ResponseModel;
import com.carhut.proxy.model.UnifiedRESTRequestModel;
import com.carhut.proxy.processor.RequestTaskProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class ProxyController {
    private RequestTaskProcessor processor = new RequestTaskProcessor();

    @RateLimitingProtection
    @RequestMapping(
            value = "/**",
            method = {RequestMethod.GET, RequestMethod.POST},
            consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseModel processRequest(
            HttpServletRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // for adding car images
            @RequestPart(value = "json", required = false) String json, // for adding car object
            @RequestBody(required = false) String body,
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestHeader(value = "Content-Type", required = false) String contentType
    ) throws ExecutionException, InterruptedException {
        ResponseModel responseModel = processor.processRequest(new UnifiedRESTRequestModel(
                request, body, bearerToken, contentType, files, json)).get();
        return responseModel;
    }
}
