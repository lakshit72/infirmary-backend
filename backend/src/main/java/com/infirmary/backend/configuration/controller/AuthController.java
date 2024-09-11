package com.infirmary.backend.configuration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.service.AuthService;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/patient/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest){
        return authService.loginServicePat(loginRequest);
    }
    @PostMapping("/patient/signup")
    public ResponseEntity<?> registerUser(@RequestBody PatientDTO patientDTO){
        return authService.signUpPat(patientDTO);
    }
}