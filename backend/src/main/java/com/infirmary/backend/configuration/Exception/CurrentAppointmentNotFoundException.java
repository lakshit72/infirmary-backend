package com.infirmary.backend.configuration.Exception;

public class CurrentAppointmentNotFoundException extends RuntimeException{
    public CurrentAppointmentNotFoundException(String message){
        super(message);
    }
}
