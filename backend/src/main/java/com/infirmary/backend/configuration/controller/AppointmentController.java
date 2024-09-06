package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping(value = "/{appointment-id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointment-id") Long appointmentId)
            throws AppointmentNotFoundException {
        try {
            AppointmentDTO response = appointmentService.getAppointmentById(appointmentId);
            return createSuccessResponse(response);
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment Not Found");
        }
    }

    @GetMapping(value = "/byPatient/{patient-id}")
    public ResponseEntity<?> getAppointmentByPatientId(@PathVariable("patient-id") String email)
            throws PatientNotFoundException, AppointmentNotFoundException {
        try {
            List<AppointmentDTO> response = appointmentService.getAppointmentsByPatientId(email);
            return createSuccessResponse(response);
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment Not Found");
        }
    }

    @GetMapping(value = "/byDoctor/{doctor-id}")
    public ResponseEntity<?> getAppointmentByDoctorId(@PathVariable("doctor-id") Long doctorId)
            throws DoctorNotFoundException, AppointmentNotFoundException {
        try {
            List<AppointmentDTO> response = appointmentService.getAppointmentsByDoctorId(doctorId);
            return createSuccessResponse(response);
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment Not Found");
        }
    }

    @GetMapping(value = "/byPrescription/{url}")
    public ResponseEntity<?> getAppointmentByPrescriptionUrl(@PathVariable("url") String url) {
        try {
            AppointmentDTO response = appointmentService.getAppointmentByPrescriptionUrl(url);
            return createSuccessResponse(response);
        } catch (AppointmentNotFoundException e) {
            return ResponseEntity.status(404).body("Appointment Not Found");
        }
    }
}