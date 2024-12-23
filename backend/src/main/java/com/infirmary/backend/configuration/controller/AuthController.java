package com.infirmary.backend.configuration.controller;


import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;
import com.infirmary.backend.configuration.service.AuthService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    private AuthService authService;

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody PatientReqDTO patientDTO, BindingResult bindingResult) throws UserAlreadyExists, IOException, MessagingException{
        return authService.signUpPat(patientDTO);
    }

    @GetMapping("/user/verify")
    public ResponseEntity<?> verifyPatient(@RequestParam(name = "code") UUID code){
        return createSuccessResponse(authService.verifyUser(code));
    }

    @PostMapping("/test")
    public List<?> test(){
        throw new IllegalAccessError();
    }
}