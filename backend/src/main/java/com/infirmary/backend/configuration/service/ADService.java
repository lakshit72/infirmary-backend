package com.infirmary.backend.configuration.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ADService {
    ResponseEntity<?> getQueue();
    ResponseEntity<?> getPatientFormDetails(String sapEmail);
    ResponseEntity<?> getCompletedQueue();
}
