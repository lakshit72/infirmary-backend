package com.infirmary.backend.configuration.Exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class StockNotFoundException extends ResourceNotFoundException {
    public StockNotFoundException(String message) {
        super(message);
    }
}
