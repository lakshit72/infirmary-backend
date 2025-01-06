package com.infirmary.backend.configuration.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PasswordChangeDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;

import jakarta.mail.MessagingException;

@Service
public interface AuthService {


    public ResponseEntity<?> loginServicePat(LoginRequestDTO loginRequestDTO);

    public ResponseEntity<?> signUpPat(PatientReqDTO patientDTO) throws UserAlreadyExists, IOException, MessagingException;

    public ResponseEntity<?> signUpDat(DoctorDTO doctorDTO) throws UserAlreadyExists, MessagingException, UnsupportedEncodingException;

    public ResponseEntity<?> signUpAD(AdDTO adDTO) throws UserAlreadyExists, UnsupportedEncodingException, MessagingException;

    public ResponseEntity<?> loginServiceAd(LoginRequestDTO loginRequestDTO,Double latitude, Double longitude);

    public ResponseEntity<?> loginServiceDat(LoginRequestDTO loginRequestDTO);

    public ResponseEntity<?> loginServiceAdmin(LoginRequestDTO loginRequestDTO);

    public String verifyUser(UUID code);

    public String forgetPassPat(String email) throws UnsupportedEncodingException, MessagingException;
    
    public String forgetPassDoc(String email) throws UnsupportedEncodingException, MessagingException;
    
    public String forgetPassAd(String email) throws UnsupportedEncodingException, MessagingException;
    
    public String changePassPat(UUID code, PasswordChangeDTO passwordChangeDTO) throws UnsupportedEncodingException, MessagingException;

    public String changePassDoc(UUID code, PasswordChangeDTO passwordChangeDTO) throws UnsupportedEncodingException, MessagingException;

    public String changePassAd(UUID code, PasswordChangeDTO passwordChangeDTO) throws UnsupportedEncodingException, MessagingException;

}
