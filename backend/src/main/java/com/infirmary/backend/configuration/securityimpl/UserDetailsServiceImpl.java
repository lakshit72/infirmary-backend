package com.infirmary.backend.configuration.securityimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.RolesNotFound;

import jakarta.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private PatientDetailsImpl patientDetailsImpl;
    private DoctorDetailsImpl doctorDetailsImpl;
    private AdDetailsImpl adDetailsImpl;

    List<UserDetailsService> usrList;

    public UserDetailsServiceImpl(PatientDetailsImpl patientDetailsImpl,DoctorDetailsImpl doctorDetailsImpl,AdDetailsImpl adDetailsImpl){
        List<UserDetailsService> temp = new ArrayList<>();
        this.patientDetailsImpl = patientDetailsImpl;
        this.doctorDetailsImpl = doctorDetailsImpl;
        this.adDetailsImpl = adDetailsImpl;
        temp.add(this.patientDetailsImpl);
        temp.add(this.doctorDetailsImpl);
        temp.add(this.adDetailsImpl);
        this.usrList = temp;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException ,RolesNotFound {
        for(UserDetailsService usrServ:usrList){
            try{
                UserDetails currUser = usrServ.loadUserByUsername(username);
                return currUser;
            }catch(UsernameNotFoundException err){
                continue;
            }
        }
        throw new UsernameNotFoundException("Bad Credentials");
    }
   
}
