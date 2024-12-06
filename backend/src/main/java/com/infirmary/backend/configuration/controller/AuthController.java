package com.infirmary.backend.configuration.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    PrescriptionRepository prescriptionRepository;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("patient/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest){
        return authService.loginServicePat(loginRequest);
    }
    @PostMapping("ad/signin")
    public ResponseEntity<?> authenticateAd(@RequestBody LoginRequestDTO loginRequestDTO,@RequestHeader(name = "X-Latitude",required = true) Double latitude,@RequestHeader(name = "X-Longitude", required = true) Double longitude){
        return authService.loginServiceAd(loginRequestDTO,latitude,longitude);
    }
    @PostMapping("doc/signin")
    public ResponseEntity<?> authenticateDoc(@RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginServiceDat(loginRequestDTO);
    }

    @PostMapping("/patient/signup")
    public ResponseEntity<?> registerUser(@RequestBody PatientReqDTO patientDTO) throws UserAlreadyExists, IOException{
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
    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestHeader(name = "Location-Latitude") String latitude){
        return ResponseEntity.ok(latitude);
    }
}