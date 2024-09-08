package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class MedicalDetailsNotFoundException extends ResourceNotFoundException {
    public MedicalDetailsNotFoundException(String message){
        super(message);
    }
}
