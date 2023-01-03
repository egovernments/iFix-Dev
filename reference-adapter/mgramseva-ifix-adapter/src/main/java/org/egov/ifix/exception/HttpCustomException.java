package org.egov.ifix.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
public class HttpCustomException extends RuntimeException{
    private String code;
    private String message;
    private Map<String, String> errors;
    private HttpStatus httpStatus = null;
    private String description;

    public HttpCustomException() {
    }

    public HttpCustomException(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpCustomException(Map<String, String> errors, HttpStatus httpStatus) {
        this.message = errors.toString();
        this.errors = errors;
        this.httpStatus = httpStatus;
    }
    public HttpCustomException(Map<String, String> errors) {
        this.message = errors.toString();
        this.errors = errors;
    }

    public HttpCustomException(String code, String message, String description, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
