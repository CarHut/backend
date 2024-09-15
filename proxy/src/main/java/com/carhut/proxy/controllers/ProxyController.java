package com.carhut.proxy.controllers;

import com.carhut.proxy.models.dtos.SerializedResponseDto;
import com.carhut.proxy.processors.RequestProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.CompletableFuture;

@Controller
public class ProxyController {

    @Autowired
    private RequestProcessor requestProcessor;

    @RequestMapping(
            value = "/*",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public CompletableFuture<SerializedResponseDto> trafficRoutingEndpoint(
            HttpServletRequest request,
            @RequestBody(required = false) String payload,
            @RequestHeader(value = "Authorization", required = false) String bearerToken,
            @RequestHeader(value = "Content-Type", required = false) String contentType
    ) {
        return requestProcessor.processRequestAsync(request, payload, bearerToken, contentType);
    }

}
