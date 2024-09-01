package com.infirmary.backend.configuration.Exception;

public class MedicalDetailsNotFoundException extends RuntimeException{
    public MedicalDetailsNotFoundException(String message){
        super(message);
    }
}
