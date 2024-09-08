package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import javax.print.Doc;

public class DoctorNotFoundException extends ResourceNotFoundException {
    public DoctorNotFoundException(String message){super(message);}
}
