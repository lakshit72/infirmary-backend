package com.infirmary.backend.configuration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest){
        return authService.loginServicePat(loginRequest);
    }
    @PostMapping("/patient/signup")
    public ResponseEntity<?> registerUser(@RequestBody PatientDTO patientDTO){
        return authService.signUpPat(patientDTO);
    }
    @PostMapping("/doctor/signup")
    public ResponseEntity<?> registerDoc(@RequestBody DoctorDTO doctorDTO){
        return authService.signUpDat(doctorDTO);
    }
    @PostMapping("/AD/signup")
    public ResponseEntity<?> registerAD(@RequestBody AdDTO adDTO){
        return authService.signUpAD(adDTO);
    }
}