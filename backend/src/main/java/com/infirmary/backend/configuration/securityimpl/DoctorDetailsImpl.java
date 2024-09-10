package com.infirmary.backend.configuration.securityimpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.repository.DoctorRepository;

import jakarta.transaction.Transactional;

@Service
public class DoctorDetailsImpl implements UserDetailsService{
    private DoctorRepository doctorRepository;

    public DoctorDetailsImpl(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Doctor doctor = doctorRepository.findByDoctorEmail(username).orElseThrow(()-> new UsernameNotFoundException("Doctor Does not Exists"));

        return UserDetailsImpl.build(doctor);
    }

}