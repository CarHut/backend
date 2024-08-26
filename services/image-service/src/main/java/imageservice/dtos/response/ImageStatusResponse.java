package imageservice.dtos.response;

import models.responses.GenericResponse;

import java.util.List;

public class ImageStatusResponse<T> extends GenericResponse<T> {
    public ImageStatusResponse(String status, int code, String message, T images) {
        super(status, code, message, images);
    }
}
