package com.carhut.proxy.controller.ratelimit;

import com.carhut.proxy.model.UnifiedRESTResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException extends RuntimeException {
    public RateLimitException(final String message) {
        super(message);
    }

    public UnifiedRESTResponseModel toResponseModel() {
        return new UnifiedRESTResponseModel(null, HttpStatus.TOO_MANY_REQUESTS.value(), "Rate limited.");
    }

}
