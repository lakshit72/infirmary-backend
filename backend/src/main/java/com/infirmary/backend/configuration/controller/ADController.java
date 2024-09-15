package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.service.ADService;
import com.infirmary.backend.configuration.service.DoctorService;

import jakarta.websocket.server.PathParam;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/AD")
public class ADController {
    private final DoctorService doctorService;
    private final ADService adService;

    public ADController(ADService adService, DoctorService doctorService){
        this.adService = adService;

        this.doctorService = doctorService;
    }

    @GetMapping(value = "/getAvailableDoctors")
    public ResponseEntity<?> getAllAvailableDoctors() throws DoctorNotFoundException {
        List<DoctorDTO> list = doctorService.getAvailableDoctors();
        return createSuccessResponse(list);
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAptForm/{sapEmail}")
    public ResponseEntity<?> getAptForm(@PathParam("sapEmail") String sapEmail){
        return adService.getPatientFormDetails(sapEmail);
    }
    
    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getAllDoctors")
    public ResponseEntity<?> getAllDoctors() throws DoctorNotFoundException {
        List<DoctorDTO> response = doctorService.getAllDoctors();
        return createSuccessResponse(response);
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getPatientQueue")
    public ResponseEntity<?> getPatientQueue(){
        return adService.getQueue();
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getCompletedQueue")
    public ResponseEntity<?> getCompletedQueue(){
        return adService.getCompletedQueue();
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/submitAppointment")
    public ResponseEntity<?> submitAppointment(@RequestBody AdSubmitReqDTO adSubmitReqDTO){
        return ResponseEntity.ok(adService.submitAppointment(adSubmitReqDTO));
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/rejectAppointment")
    public ResponseEntity<?> rejectAppointment(@RequestParam("email") String email){
        return ResponseEntity.ok(adService.rejectAppointment(email));
    }
}
