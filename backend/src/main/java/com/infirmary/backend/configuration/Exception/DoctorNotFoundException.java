package com.infirmary.backend.configuration.Exception;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(String message) {
        super(message);
    }

    public DoctorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
