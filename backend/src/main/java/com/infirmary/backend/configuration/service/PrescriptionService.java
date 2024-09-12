package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import org.springframework.stereotype.Service;

@Service
public interface PrescriptionService {
    void submitPrescription(PrescriptionDTO prescriptionDTO);
}
