package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import com.infirmary.backend.configuration.service.CurrentAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/currentAppointment")
public class CurrentAppointmentController {
    private final CurrentAppointmentService currentAppointmentService;

    public CurrentAppointmentController(CurrentAppointmentService currentAppointmentService) {
        this.currentAppointmentService = currentAppointmentService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCurrentAppointmentById(@PathVariable("id") Long id) {
        CurrentAppointmentDTO currentAppointment = currentAppointmentService.getCurrentAppointmentById(id);
        return createSuccessResponse(currentAppointment);
    }
}
