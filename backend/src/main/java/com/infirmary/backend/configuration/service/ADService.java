package com.infirmary.backend.configuration.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.AdHocSubmitDTO;
import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.dto.ReassignPatientDTO;

@Service
public interface ADService {
    ResponseEntity<?> getQueue(Double latitude, Double longitude);
    ResponseEntity<?> getPatientFormDetails(String sapEmail);
    ResponseEntity<?> getCompletedQueue(Double latitude, Double longitude);
    String submitAppointment(AdSubmitReqDTO adSubmitReqDTO);
    String rejectAppointment(String email) throws IOException;
    String setDocStatus(UUID docID,Boolean docStat,Double latitude,Double longitude);
    String completeAppointment(String sapEmail);
    ResponseEntity<?> getTokenData(String email);
    List<?> getAssignedPatient(String sapEmail);
    List<?> getAppointmentByDate(LocalDate date, String sapEmail);
    String submitAdHocAppointment(AdHocSubmitDTO adHocSubmitDTO, String adEmail);
    List<?> getAdHocAppointment(LocalDate date);
    String reassignPatient(ReassignPatientDTO reassignPatientDTO);
}
