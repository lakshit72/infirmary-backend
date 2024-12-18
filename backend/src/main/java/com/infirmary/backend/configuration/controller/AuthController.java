package com.infirmary.backend.configuration.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.service.AuthService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private AuthService authService;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("patient/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest,BindingResult bindingResult){
        return authService.loginServicePat(loginRequest);
    }

    @PostMapping("ad/signin")
    public ResponseEntity<?> authenticateAd(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult bindingResult,@RequestHeader(name = "X-Latitude",required = true) Double latitude,@RequestHeader(name = "X-Longitude", required = true) Double longitude){
        return authService.loginServiceAd(loginRequestDTO,latitude,longitude);
    }

    @PostMapping("doctor/signin")
    public ResponseEntity<?> authenticateDoc(@Valid @RequestBody LoginRequestDTO loginRequestDTO,BindingResult bindingResult){
        return authService.loginServiceDat(loginRequestDTO);
    }

    @PostMapping("admin/signin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequestDTO loginRequest, BindingResult bindingResult){
        return authService.loginServiceAdmin(loginRequest);
    }

    @PostMapping("/patient/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody PatientReqDTO patientDTO, BindingResult bindingResult) throws UserAlreadyExists, IOException{
        return authService.signUpPat(patientDTO);
    }

    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestHeader(name = "Location-Latitude") String latitude){
        throw new ResourceNotFoundException("Test Exceptiopn");
    }
}