package com.infirmary.backend.configuration.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;

@Service
public interface ADService {
    ResponseEntity<?> getQueue();
    ResponseEntity<?> getPatientFormDetails(String sapEmail);
    ResponseEntity<?> getCompletedQueue();
    String submitAppointment(AdSubmitReqDTO adSubmitReqDTO);
    String rejectAppointment(String email);
}
