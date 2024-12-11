package com.infirmary.backend.configuration.securityimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.RolesNotFound;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private PatientDetailsImpl patientDetailsImpl;

    public UserDetailsServiceImpl(PatientDetailsImpl patientDetailsImpl){
        this.patientDetailsImpl = patientDetailsImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException ,RolesNotFound,ResourceNotFoundException {
        
        UserDetails currUser = patientDetailsImpl.loadUserByUsername(username);
        return currUser;
        
    }
   
}
