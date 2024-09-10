package com.infirmary.backend.configuration.Exception;

public class StockAlreadyExists extends RuntimeException {
    public StockAlreadyExists(String message) {
        super(message);
    }
}
