package com.infirmary.backend.configuration.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface StaticService {
    public byte[] imageReturn(String filename) throws ResourceNotFoundException;
}
