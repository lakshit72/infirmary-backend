package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.MedicalDetailsNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.SapIdExistException;
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

    @GetMapping(value = "/test")
    public String test(){
        return "Hello world";
    }

    @GetMapping(value = "/{sap-id}")
    public ResponseEntity<?> getPatientBySapId(@PathVariable("sap-id") Long sapId)
            throws PatientNotFoundException {
        PatientDTO response = patientService.getPatientBySapId(sapId);
        return createSuccessResponse(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createPatient(PatientDTO patientDTO, MedicalDetailsDTO medicalDetailsDTO) throws SapIdExistException {
        patientService.validatePatientData(patientDTO);
        PatientDetailsResponseDTO response = patientService.createPatient(patientDTO, medicalDetailsDTO);
        return createSuccessResponse(response);
    }

    @PutMapping("/update/{sap-id}")
    public ResponseEntity<?> updatePatient(@PathVariable("sap-id") Long sapId, @RequestBody PatientDTO patientDTO,
                                           @RequestBody MedicalDetailsDTO medicalDetailsDTO) throws
            PatientNotFoundException, MedicalDetailsNotFoundException {
        PatientDetailsResponseDTO response = patientService.updatePatientDetails(sapId, patientDTO, medicalDetailsDTO);
        return createSuccessResponse(response);
    }

}
