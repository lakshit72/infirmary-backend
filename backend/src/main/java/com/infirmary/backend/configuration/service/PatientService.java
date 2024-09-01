package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;

public interface PatientService {
    PatientDTO getPatientBySapId(Long sapId) throws PatientNotFoundException;
    PatientDetailsResponseDTO createPatient(PatientDTO patientDTO, MedicalDetailsDTO medicalDetailsDTO);
    void validatePatientData(PatientDTO patientDTO);
    PatientDetailsResponseDTO updatePatientDetails(Long sapId,
                                                   PatientDTO patientDTO,
                                                   MedicalDetailsDTO medicalDetailsDTO)
            throws PatientNotFoundException, MedicalDetailsNotFoundException;
}
