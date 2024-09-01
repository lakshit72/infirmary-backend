package com.infirmary.backend.configuration.Exception;

public class CurrentAppointmentNotFoundException extends RuntimeException {

    public CurrentAppointmentNotFoundException(String message) {
        super(message);
    }

    public CurrentAppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
