package com.infirmary.backend.configuration.controller;

@RestController
@RequestMapping("/api/patient")
public class PatientController{
    
    private final PatientService patientService;

    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }

    @PostMapping(value="/{sapId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPatientBySapId(@PathVariable Long sapId) throws PatientNotFoundException {
        PatientDTO patientDTO = patientService.getPatientBySapId(sapId);
        return ResponseEntity.ok(patientDTO);
    }
}