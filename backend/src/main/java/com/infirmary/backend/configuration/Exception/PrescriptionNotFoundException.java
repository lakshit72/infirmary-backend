package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class PrescriptionNotFoundException extends ResourceNotFoundException {
    public PrescriptionNotFoundException(String message) {
        super(message);
    }
}
