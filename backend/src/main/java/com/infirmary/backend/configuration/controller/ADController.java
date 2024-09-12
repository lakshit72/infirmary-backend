package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.repository.AdRepository;

import java.util.List;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/AD")
public class ADController {
    private final AdRepository adRepository;
    private final DoctorService doctorService;

    public ADController(AdRepository adRepository, DoctorService doctorService){
        this.adRepository = adRepository;

        this.doctorService = doctorService;
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping("/getQueue")
    public ResponseEntity<?> getQueue(){

        return null;
    }
    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getAvailableDoctors")
    public ResponseEntity<?> getAllAvailableDoctors() throws DoctorNotFoundException {
        List<DoctorDTO> list = doctorService.getAvailableDoctors();
        return createSuccessResponse(list);
    }
    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getAllDoctors")
    public ResponseEntity<?> getAllDoctors() throws DoctorNotFoundException {
        List<DoctorDTO> response = doctorService.getAllDoctors();
        return createSuccessResponse(response);
    }
}
