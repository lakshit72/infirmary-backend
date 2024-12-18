package com.infirmary.backend.configuration.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;

@Service
public interface ADService {
    ResponseEntity<?> getQueue(Double latitude, Double longitude);
    ResponseEntity<?> getPatientFormDetails(String sapEmail);
    ResponseEntity<?> getCompletedQueue(Double latitude, Double longitude);
    String submitAppointment(AdSubmitReqDTO adSubmitReqDTO);
    String rejectAppointment(String email);
    String setDocStatus(Long docID,Boolean docStat,Double latitude,Double longitude);
    String completeAppointment(String sapEmail);
    ResponseEntity<?> getTokenData(String email);
    List<?> getAssignedPatient(String sapEmail);
    List<?> getAppointmentByDate(LocalDate date, String sapEmail);
}
