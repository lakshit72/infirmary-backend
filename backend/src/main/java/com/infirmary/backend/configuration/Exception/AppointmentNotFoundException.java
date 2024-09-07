package com.infirmary.backend.configuration.Exception;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException(String message){
        super(message);
    }
}
