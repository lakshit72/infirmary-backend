package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.SapIdExistException;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping(value = "/{sap-id}")
    public ResponseEntity<?> getPatientBySapId(@PathVariable("sap-id") Long sapId)
            throws PatientNotFoundException {
        PatientDTO response = patientService.getPatientBySapId(sapId);
        return createSuccessResponse(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createPatient(PatientDTO patientDTO) throws SapIdExistException {
        patientService.validatePatientData(patientDTO);
        PatientDTO response = patientService.createPatient(patientDTO);
        return createSuccessResponse(response);
    }
}
