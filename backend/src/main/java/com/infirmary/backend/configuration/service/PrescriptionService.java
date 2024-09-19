package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.dto.PrescriptionReq;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PrescriptionService {
    void submitPrescription(PrescriptionReq prescriptionDTO);

    ResponseEntity<?> getPrescription(Long id);
}
