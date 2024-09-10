package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;
import com.infirmary.backend.configuration.service.PatientService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/{sap-email}")
    public ResponseEntity<?> getPatientBySapEmail(@PathVariable("sap-email") String sapEmail)
            throws PatientNotFoundException {
        PatientDTO response = patientService.getPatientBySapEmail(sapEmail);
        return createSuccessResponse(response);
    }

    @PutMapping(value = "/update/{sap-email}")
    public ResponseEntity<?> updatePatient(@PathVariable("sap-email") String sapEmail,
                                           @RequestBody MedicalDetailsDTO medicalDetailsDTO) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        MedicalDetailsDTO response = patientService.updatePatientDetails(sapEmail, medicalDetailsDTO);
        return createSuccessResponse(response);
    }

    @GetMapping(value = "/getAllDetails/{sap-email}")
    public ResponseEntity<?> getAllDetails(@PathVariable("sap-email") String sapEmail) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        PatientDetailsResponseDTO response = patientService.getAllDetails(sapEmail);
        return createSuccessResponse(response);
    }

    @GetMapping(value = "/get")
    public String TestAPI(){
        return "hello";
    }
}
