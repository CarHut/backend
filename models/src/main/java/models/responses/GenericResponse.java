package models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse<T> {
     private String status;
     private int code;
     private String message;
     private T body;

    public GenericResponse(String status, int code, String message, T body) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.body = body;
    }
}
