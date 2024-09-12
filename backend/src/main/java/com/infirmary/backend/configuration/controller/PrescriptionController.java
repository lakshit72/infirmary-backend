package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/prescription")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    private final CurrentAppointmentRepository currentAppointmentRepository;

    public PrescriptionController(PrescriptionService prescriptionService,
                                  CurrentAppointmentRepository currentAppointmentRepository) {
        this.prescriptionService = prescriptionService;
        this.currentAppointmentRepository = currentAppointmentRepository;
    }

    @PostMapping(value = "/submit")
    public ResponseEntity<?> submitPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        prescriptionService.submitPrescription(prescriptionDTO);
        return createSuccessResponse("Prescription submitted");
    }
}
