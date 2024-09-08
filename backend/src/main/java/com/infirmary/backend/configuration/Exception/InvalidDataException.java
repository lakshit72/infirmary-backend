package com.infirmary.backend.configuration.Exception;

public class InvalidDataException extends IllegalArgumentException {
    public InvalidDataException(String message) {
        super(message);
    }
}
