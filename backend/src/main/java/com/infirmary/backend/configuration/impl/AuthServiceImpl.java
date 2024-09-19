package com.infirmary.backend.configuration.impl;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
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
import com.infirmary.backend.configuration.dto.AdDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.JwtResponse;
import com.infirmary.backend.configuration.dto.LoginRequestDTO;
import com.infirmary.backend.configuration.dto.PatientDTO;
import com.infirmary.backend.configuration.dto.PatientReqDTO;
import com.infirmary.backend.configuration.jwt.JwtUtils;
import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.AdRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
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
    private AdRepository adRepository;
    private DoctorRepository doctorRepository;
    
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
    public ResponseEntity<?> signUpPat(PatientReqDTO patientDTO) throws UserAlreadyExists, IOException {
            if(patientRepository.existsByEmail(patientDTO.getEmail())){
                throw new UserAlreadyExists("User Already Exists");
            }

            patientDTO.setPassword(encoder.encode(patientDTO.getPassword()));
    
            Patient patient = new Patient(
                patientDTO
            );

            byte[] data = Base64.getDecoder().decode(patientDTO.getImg());

            String type = patientDTO.getImg().split(";")[0].split("/")[1];


            String filePath = "./build/resources/main/static/Profile/"+patientDTO.getEmail()+"."+type;
            String flpth = "./src/main/resources/static/Profile/"+patientDTO.getEmail()+"."+type;

            try{
                OutputStream stream = new FileOutputStream(filePath);
                OutputStream strm = new FileOutputStream(flpth);
                
                stream.write(data);
                strm.write(data);

                stream.close();
                strm.close();
            }catch (Exception e){
                throw new IOException("Cannot create a file");
            }

            patientRepository.save(patient);

            return createSuccessResponse("Patient Created");
        }

        @Override
        public ResponseEntity<?> signUpDat(DoctorDTO doctorDTO) throws UserAlreadyExists{

            if(doctorRepository.existsByDoctorEmail(doctorDTO.getDoctorEmail())){
                throw new UserAlreadyExists("Doctor Already Exists");
            }

            doctorDTO.setPassword(encoder.encode(doctorDTO.getPassword()));

            Doctor doctor = new Doctor(
                doctorDTO
            );

            doctorRepository.save(doctor);

            return createSuccessResponse("Doctor Created");
        }

        @Override
        public ResponseEntity<?> signUpAD(AdDTO adDTO) throws UserAlreadyExists{
            if(adRepository.existsById(adDTO.getEmail())){
                throw new UserAlreadyExists("AD Already Exists");
            }

            adDTO.setPassword(encoder.encode(adDTO.getPassword()));

            AD ad = new AD(adDTO);

            adRepository.save(ad);

            return createSuccessResponse("AD created");
        }
        @Override
        public ResponseEntity<?> loginServiceAd(LoginRequestDTO loginRequestDTO) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.genrateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            if(!roles.get(0).equals("ROLE_AD")) throw new SecurityException("Bad Credentials");

            return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
        }
        @Override
        public ResponseEntity<?> loginServiceDat(LoginRequestDTO loginRequestDTO) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.genrateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

            if(!roles.get(0).equals("ROLE_DOCTOR")) throw new SecurityException("Bad Credentials");

            return ResponseEntity.ok(new JwtResponse(jwt,userDetails.getUsername(),roles));
        }

}
