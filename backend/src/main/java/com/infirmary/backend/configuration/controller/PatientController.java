package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
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

    @GetMapping(value = "/")
    public ResponseEntity<?> getPatientBySapEmail()
            throws PatientNotFoundException {
        System.out.println("recv");
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();
        PatientDTO response = patientService.getPatientBySapEmail(sapEmail);
        return createSuccessResponse(response);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> updatePatient(@RequestBody MedicalDetailsDTO medicalDetailsDTO) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();
        MedicalDetailsDTO response = patientService.updatePatientDetails(sapEmail, medicalDetailsDTO);
        return createSuccessResponse(response);
    }

    @GetMapping(value = "/getAllDetails")
    public ResponseEntity<?> getAllDetails() throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();
        PatientDetailsResponseDTO response = patientService.getAllDetails(sapEmail);
        return createSuccessResponse(response);
    }
}
