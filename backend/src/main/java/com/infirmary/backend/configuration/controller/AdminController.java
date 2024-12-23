package com.infirmary.backend.configuration.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.service.AuthService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/doctor/signup")
    public ResponseEntity<?> registerDoc(@RequestBody DoctorDTO doctorDTO) throws UserAlreadyExists, UnsupportedEncodingException, MessagingException{
        return authService.signUpDat(doctorDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/AD/signup")
    public ResponseEntity<?> registerAD(@RequestBody AdDTO adDTO) throws UserAlreadyExists, UnsupportedEncodingException, MessagingException{
        return authService.signUpAD(adDTO);
    }
}
