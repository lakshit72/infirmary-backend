package com.infirmary.backend.configuration.service;

public interface PatientService {

    PatientDTO getPatientBySapId(Long sapId) throws PatientNotFoundException;

}
