package com.infirmary.backend.configuration.securityimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.Exception.RolesNotFound;

import jakarta.annotation.PostConstruct;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    private PatientDetailsImpl patientDetailsImpl;
    private DoctorDetailsImpl doctorDetailsImpl;
    private AdDetailsImpl adDetailsImpl;
    private AdminDetailsImpl adminDetailsImpl;

    List<UserDetailsService> usrList;

    public UserDetailsServiceImpl(PatientDetailsImpl patientDetailsImpl,DoctorDetailsImpl doctorDetailsImpl,AdDetailsImpl adDetailsImpl,AdminDetailsImpl adminDetailsImpl){
        this.patientDetailsImpl = patientDetailsImpl;
        this.doctorDetailsImpl = doctorDetailsImpl;
        this.adDetailsImpl = adDetailsImpl;
        this.adminDetailsImpl = adminDetailsImpl;
    }

    @PostConstruct
    public void setServices(){
        List<UserDetailsService> new_ser = new ArrayList<>();
        new_ser.add(this.adDetailsImpl);
        new_ser.add(this.doctorDetailsImpl);
        new_ser.add(this.patientDetailsImpl);
        new_ser.add(this.adminDetailsImpl);
        this.usrList = new_ser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException ,RolesNotFound,ResourceNotFoundException {
        for(UserDetailsService usrServ:usrList){
            try{
                UserDetails currUser = usrServ.loadUserByUsername(username);
                return currUser;
            }catch(UsernameNotFoundException err){
                continue;
            }
        }
        throw new ResourceNotFoundException("No User Found");
    }
   
}
