package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.PatientDetails;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/doctor")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public static String getTokenClaims(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        return id;
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/byId")
    public ResponseEntity<?> getDoctorById() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        DoctorDTO response = doctorService.getDoctorById(id);
        return createSuccessResponse(response);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/getStatus")
    public ResponseEntity<?> getDoctorStatusById() throws DoctorNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        Boolean doctorStatusById = doctorService.getDoctorStatusById(id);
        return createSuccessResponse(doctorStatusById);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/setStatus")
    public ResponseEntity<?> setDoctorStatus(@RequestParam("isDoctorCheckIn")
                                             Boolean isDoctorCheckIn) throws DoctorNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        Doctor doctorStatus = doctorService.setDoctorStatus(id, isDoctorCheckIn);
        return createSuccessResponse(doctorStatus.getName());
    }
    
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/total-patient-count")
    public ResponseEntity<?> getAppointmentCountByDate()
            throws AppointmentNotFoundException {
        HashMap<String, Long> countByDate = doctorService.getAppointmentCountByDate(LocalDate.now());
        return createSuccessResponse(countByDate);
    }
    
    @PreAuthorize("hasRole('ROLE_DOCTOR') or hasRole('ROLE_AD')")
    @GetMapping(value = "/prescription/{email}")
    public ResponseEntity<?> getPrescriptionByEmail(@PathVariable("email") String email)
    {
        HashMap<LocalDate, Prescription> prescriptionHistory = doctorService.getPrescriptionHistory(email);
        return createSuccessResponse(prescriptionHistory);
    }
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getPatient")
    public ResponseEntity<?> getPatient(){
        PatientDetails patient = doctorService.getPatient(getTokenClaims());
        return ResponseEntity.ok(patient);
    }
}
