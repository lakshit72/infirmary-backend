package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.PatientDTO;

public interface PatientService {
    PatientDTO getPatientBySapId(Long sapId) throws PatientNotFoundException;
    PatientDTO createPatient(PatientDTO patientDTO);
    void validatePatientData(PatientDTO patientDTO);
}
