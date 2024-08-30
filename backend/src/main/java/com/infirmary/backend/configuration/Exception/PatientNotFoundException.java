package com.infirmary.backend.configuration.Exception;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(String message){
        super(message);
    }
}
