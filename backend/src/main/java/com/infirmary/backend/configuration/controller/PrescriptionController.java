package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.dto.PrescriptionReq;
import com.infirmary.backend.configuration.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/prescription")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping(value = "/submit")
    public ResponseEntity<?> submitPrescription(@RequestBody PrescriptionReq prescriptionDTO) {
        prescriptionService.submitPrescription(prescriptionDTO);
        return createSuccessResponse("Prescription submitted");
    }

    @PostAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getPrescription/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable Long id) {
        return prescriptionService.getPrescription(id);
    }
}
