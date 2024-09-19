package com.infirmary.backend.configuration.service;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;

@Service
public interface AuthService {


    public ResponseEntity<?> loginServicePat(LoginRequestDTO loginRequestDTO);

    public ResponseEntity<?> signUpPat(PatientReqDTO patientDTO) throws UserAlreadyExists, IOException;

    public ResponseEntity<?> signUpDat(DoctorDTO doctorDTO);

    public ResponseEntity<?> signUpAD(AdDTO adDTO);

    public ResponseEntity<?> loginServiceAd(LoginRequestDTO loginRequestDTO);

    public ResponseEntity<?> loginServiceDat(LoginRequestDTO loginRequestDTO);
}
