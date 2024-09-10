package com.infirmary.backend.configuration.impl;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.UserAlreadyExists;
import com.infirmary.backend.configuration.dto.JwtResponse;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.jwt.JwtUtils;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.PatientRepository;
import com.infirmary.backend.configuration.securityimpl.UserDetailsImpl;
import com.infirmary.backend.configuration.service.AuthService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    
    private AuthenticationManager authenticationManager;
    private PatientRepository patientRepository;
    private PasswordEncoder encoder;
    private JwtUtils jwtUtils;
    
    @Override
    public ResponseEntity<?> loginServicePat(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.genrateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
    }
    @Override
    public ResponseEntity<?> signUpPat(PatientDTO patientDTO) throws UserAlreadyExists {
            if(patientRepository.existsByEmail(patientDTO.getEmail())){
                throw new UserAlreadyExists("User Already Exists");
            }

            patientDTO.setPassword(encoder.encode(patientDTO.getPassword()));
    
            Patient patient = new Patient(
                patientDTO
            );
            
            patientRepository.save(patient);

            return createSuccessResponse("Patient Created");
        }

}