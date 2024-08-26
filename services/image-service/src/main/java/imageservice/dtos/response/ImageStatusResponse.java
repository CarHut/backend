package imageservice.dtos.response;

public record ImageStatusResponse<T>(String status, String message, T body) {}
