package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.PatientNotFoundException;
import com.infirmary.backend.configuration.Exception.PrescriptionNotFoundException;
import com.infirmary.backend.configuration.dto.AppointmentDTO;
import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/{appointment-id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable("appointment-id") Long appointmentId)
            throws AppointmentNotFoundException {
        AppointmentDTO response = appointmentService.getAppointmentById(appointmentId);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/byPatient")
    public ResponseEntity<?> getAppointmentByPatientId()
            throws PatientNotFoundException, AppointmentNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        List<AppointmentDTO> response = appointmentService.getAppointmentsByPatientId(email);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/byDoctor")
    public ResponseEntity<?> getAppointmentByDoctorId()
            throws DoctorNotFoundException, AppointmentNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String doctorId = userDetails.getUsername();
        List<AppointmentDTO> response = appointmentService.getAppointmentsByDoctorId(doctorId);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/doctor/next-appointment")
    public ResponseEntity<?> getNextAppointmentForDoctor() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();
        AppointmentDTO currentNextAppointment = appointmentService.getCurrentNextAppointment();
        return createSuccessResponse(currentNextAppointment);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/prescriptionUrls/byEmail")
    public ResponseEntity<?> getPrescriptionUrlByPatientEmail()
            throws PatientNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        List<Prescription> response = appointmentService.getPrescriptionUrlByPatientEmail(email);
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/prescription/byAppointmentId/{app-id}")
    public ResponseEntity<?> getPrescriptionByAppointmentId(@PathVariable("app-id") Long appointmentId)
            throws AppointmentNotFoundException, PrescriptionNotFoundException {
        PrescriptionDTO response = appointmentService.getPrescriptionByAppointmentId(appointmentId);
        return createSuccessResponse(response);
    }

    @GetMapping(value = "/lastAppointmentDate")
    public ResponseEntity<?> getLastAppointmentDate() throws AppointmentNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();
        LocalDate response = appointmentService.getLastAppointmentDateByEmail(email);
        return createSuccessResponse(response);
    }

}
