package org.egov.ifix.exception;

import lombok.Data;

import java.util.Map;

@Data
public class GenericCustomException extends RuntimeException{
    private String code;
    private String message;
    private Map<String, String> errors;

    public GenericCustomException(String code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public GenericCustomException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public GenericCustomException(Map<String, String> errors) {
        this.errors = errors;
    }
}
