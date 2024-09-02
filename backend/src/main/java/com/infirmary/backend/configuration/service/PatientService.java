package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;

public interface PatientService {
    PatientDTO getPatientBySapEmail(Long sapId) throws PatientNotFoundException;
    void validatePatientData(PatientDTO patientDTO);
    MedicalDetailsDTO updatePatientDetails(Long sapId,
                                                   MedicalDetailsDTO medicalDetailsDTO)
            throws PatientNotFoundException, MedicalDetailsNotFoundException;

    PatientDetailsResponseDTO getAllDetails(Long sapId) throws PatientNotFoundException, MedicalDetailsNotFoundException;
}
