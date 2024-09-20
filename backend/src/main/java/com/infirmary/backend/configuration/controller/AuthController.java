package com.infirmary.backend.configuration.controller;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;
import com.infirmary.backend.configuration.dto.PrescriptionReq;
import com.infirmary.backend.configuration.model.PrescriptionMeds;
import com.infirmary.backend.configuration.model.Stock;
import com.infirmary.backend.configuration.repository.PrescriptionMedsRepository;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    @Autowired
    private PrescriptionMedsRepository prescriptionMedsRepository;
    @Autowired
    private StockRepository stockRepository;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("patient/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest){
        return authService.loginServicePat(loginRequest);
    }
    @PostMapping("ad/signin")
    public ResponseEntity<?> authenticateAd(@RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginServiceAd(loginRequestDTO);
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
    public ResponseEntity<?> test(@RequestBody PrescriptionReq inp){
        PrescriptionMeds meds = new PrescriptionMeds();
        Stock stock = stockRepository.findByBatchNumber(inp.getMeds().get(0).getMedicine()).orElseThrow(()-> new ResourceNotFoundException("No"));

        meds.setMedicine(stock);
        meds.setDosage(inp.getMeds().get(0).getDosage());
        PrescriptionMeds lst = prescriptionMedsRepository.save(meds);
        return ResponseEntity.ok(meds);
    }
}