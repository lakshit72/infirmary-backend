package com.infirmary.backend.configuration.service;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentReqDTO;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PatientService {
    PatientDTO getPatientBySapEmail(String sapEmail) throws PatientNotFoundException;

    void validatePatientData(PatientDTO patientDTO);

    MedicalDetailsDTO updatePatientDetails(String sapEmail,
                                           MedicalDetailsDTO medicalDetailsDTO)
            throws PatientNotFoundException, MedicalDetailsNotFoundException;

    PatientDetailsResponseDTO getAllDetails(String sapEmail) throws PatientNotFoundException, MedicalDetailsNotFoundException;

    ResponseEntity<?> submitAppointment(String sapEmail,AppointmentReqDTO appointmentReqDTO);

    ResponseEntity<?> getStatus(String sapEmail);

    ResponseEntity<?> getToken(String sapEmail) throws ResourceNotFoundException;

    ResponseEntity<?> getPrescriptions(String sapEmail);

    ResponseEntity<?> getAppointment(String sapEmail);
}
