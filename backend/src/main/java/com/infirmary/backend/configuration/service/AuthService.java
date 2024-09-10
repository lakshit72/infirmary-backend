package com.infirmary.backend.configuration.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;

@Service
public interface AuthService {


    public ResponseEntity<?> loginServicePat(LoginRequestDTO loginRequestDTO);

    public ResponseEntity<?> signUpPat(PatientDTO patientDTO);


}
