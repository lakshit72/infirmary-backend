package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class CurrentAppointmentNotFoundException extends ResourceNotFoundException {
    public CurrentAppointmentNotFoundException(String message){
        super(message);
    }
}
