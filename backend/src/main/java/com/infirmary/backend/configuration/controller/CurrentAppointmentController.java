package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.CurrentAppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentResDTO;
import com.infirmary.backend.configuration.dto.CurrentAppointmentDTO;
import com.infirmary.backend.configuration.service.CurrentAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/currentAppointment")
public class CurrentAppointmentController {
    private final CurrentAppointmentService currentAppointmentService;

    public CurrentAppointmentController(CurrentAppointmentService currentAppointmentService) {
        this.currentAppointmentService = currentAppointmentService;
    }
    
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getCurrentAppointmentById(@PathVariable("id") UUID id) {
        CurrentAppointmentDTO currentAppointment = currentAppointmentService.getCurrentAppointmentById(id);
        return createSuccessResponse(currentAppointment);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/status/{id}")
    public ResponseEntity<?> getAppointmentDoctorStatusById(@PathVariable("id") UUID id)
    {
        AppointmentResDTO appointmentStatusDoctorStatus = currentAppointmentService.getAppointmentStatusDoctorStatus(id);
        return createSuccessResponse(appointmentStatusDoctorStatus);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/currApp/{doctor-id}")
    public ResponseEntity<?> getCurrentAppointmentByDoctorId(@PathVariable("doctor-id") String doctorId)
    throws DoctorNotFoundException, CurrentAppointmentNotFoundException {
        CurrentAppointmentDTO response = currentAppointmentService.getCurrAppByDoctorId(doctorId);
        return createSuccessResponse(response);
    }
}
