package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentReqDTO;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;
import com.infirmary.backend.configuration.service.PatientService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.lang.String;


@RestController
@RequestMapping(value = "/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    private static String getTokenClaims(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    @GetMapping(value = "/")
    public ResponseEntity<?> getPatientBySapEmail()
            throws PatientNotFoundException {
        PatientDTO response = patientService.getPatientBySapEmail(getTokenClaims());
        return createSuccessResponse(response);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updatePatient(@RequestBody MedicalDetailsDTO medicalDetailsDTO) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        MedicalDetailsDTO response = patientService.updatePatientDetails(getTokenClaims(), medicalDetailsDTO);
        return createSuccessResponse(response);
    }

    @GetMapping(value = "/getAllDetails")
    public ResponseEntity<?> getAllDetails() throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        PatientDetailsResponseDTO response = patientService.getAllDetails(getTokenClaims());
        return createSuccessResponse(response);
    }

    @PostMapping(value = "/submitAppointment")
    public ResponseEntity<?> submitAppointmnent(@RequestBody AppointmentReqDTO appointmentReqDTO) {
        return patientService.submitAppointment(getTokenClaims(),appointmentReqDTO);
    }

    @GetMapping(value = "/getStatus")
    public ResponseEntity<?> getAppointmentStatus(){
        return patientService.getStatus(getTokenClaims());
    }

    @GetMapping(value = "/getToken")
    public ResponseEntity<?> getTokenNo(){
        return patientService.getToken(getTokenClaims());
    }

}
