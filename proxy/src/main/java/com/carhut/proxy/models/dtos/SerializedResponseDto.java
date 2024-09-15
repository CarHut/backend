package com.carhut.proxy.models.dtos;

public record SerializedResponseDto(Integer statusCode, String message, String token, String payload) {
}
