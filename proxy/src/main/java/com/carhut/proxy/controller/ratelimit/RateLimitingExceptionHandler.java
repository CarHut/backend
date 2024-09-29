package com.carhut.proxy.controller.ratelimit;

import com.carhut.proxy.model.UnifiedRESTResponseModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitingExceptionHandler {

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<UnifiedRESTResponseModel> handleException(final RateLimitException rateLimitException, final HttpServletRequest request) {
        final UnifiedRESTResponseModel restResponseModel = rateLimitException.toResponseModel();
        return new ResponseEntity<>(restResponseModel, HttpStatus.TOO_MANY_REQUESTS);
    }
}
