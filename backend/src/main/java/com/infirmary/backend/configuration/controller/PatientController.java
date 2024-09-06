package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.MedicalDetailsDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientDetailsResponseDTO;
import com.infirmary.backend.configuration.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

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
        try {
            PatientDTO response = patientService.getPatientBySapEmail(sapEmail);
            return createSuccessResponse(response);
        } catch (PatientNotFoundException err) {
            return ResponseEntity.status(404).body("Patient Not Found");
        }
    }

    @PutMapping(value = "/update/{sap-email}")
    public ResponseEntity<?> updatePatient(@PathVariable("sap-email") String sapEmail,
                                           @RequestBody MedicalDetailsDTO medicalDetailsDTO) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        try {
            MedicalDetailsDTO response = patientService.updatePatientDetails(sapEmail, medicalDetailsDTO);
            return createSuccessResponse(response);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(404).body("Patient Not Found");
        }
    }

    @GetMapping(value = "/getAllDetails/{sap-email}")
    public ResponseEntity<?> getAllDetails(@PathVariable("sap-email") String sapEmail) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        try {
            PatientDetailsResponseDTO response = patientService.getAllDetails(sapEmail);
            return createSuccessResponse(response);
        } catch (PatientNotFoundException e) {
            return ResponseEntity.status(404).body("Patient Not Found");
        }
    }
}
