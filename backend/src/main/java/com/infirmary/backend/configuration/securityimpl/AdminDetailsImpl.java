package com.infirmary.backend.configuration.securityimpl;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.model.Admin;
import com.infirmary.backend.configuration.repository.AdminRepository;

@Service
public class AdminDetailsImpl implements UserDetailsService{
    private AdminRepository adminRepository;

    public AdminDetailsImpl(AdminRepository adminRepository){
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
        Admin admin = adminRepository.findByAdminEmail(username).orElseThrow(()-> new ResourceNotFoundException("Admin Does Not Exists"));
        return UserDetailsImpl.build(admin);
    }


}
