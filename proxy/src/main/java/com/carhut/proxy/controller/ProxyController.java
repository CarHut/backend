package com.carhut.proxy.controller;

import com.carhut.proxy.model.ResponseModel;
import com.carhut.proxy.model.UnifiedRESTRequestModel;
import com.carhut.proxy.processor.RequestTaskProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
public class ProxyController {

    private RequestTaskProcessor processor = new RequestTaskProcessor();

    @RequestMapping(
            value = "/**",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ResponseModel processRequest(
            HttpServletRequest request,
            @RequestBody(required = false) String body,
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestHeader(value = "Content-Type", required = false) String contentType
    ) throws ExecutionException, InterruptedException {
        return processor.processRequest(new UnifiedRESTRequestModel(request, body, bearerToken, contentType)).get();
    }
}
