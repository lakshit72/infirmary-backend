package com.infirmary.backend.configuration.securityimpl;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.RolesNotFound;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.repository.PatientRepository;


@Service
public class PatientDetailsImpl implements UserDetailsService{
    private PatientRepository patientRepository;

    public PatientDetailsImpl(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException ,RolesNotFound {
        Patient patient = patientRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User Not Found"));

        return UserDetailsImpl.build(patient);
    }
}
