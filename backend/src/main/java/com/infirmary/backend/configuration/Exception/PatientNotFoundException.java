package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class PatientNotFoundException extends ResourceNotFoundException{
    public PatientNotFoundException(String message){
        super(message);
    }
}
