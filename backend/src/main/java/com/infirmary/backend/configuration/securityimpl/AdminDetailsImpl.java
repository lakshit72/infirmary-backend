package com.infirmary.backend.configuration.securityimpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminEmail(username).orElseThrow(()-> new UsernameNotFoundException("Admin Does Not Exists"));
        return UserDetailsImpl.build(admin);
    }


}
