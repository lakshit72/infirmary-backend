package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class DoctorNotFoundException extends ResourceNotFoundException {
    public DoctorNotFoundException(String message){super(message);}
}
