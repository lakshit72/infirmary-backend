package com.infirmary.backend.configuration.securityimpl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.repository.AdRepository;

import jakarta.transaction.Transactional;

@Service
public class AdDetailsImpl implements UserDetailsService{
    private AdRepository adRepository;

    public AdDetailsImpl(AdRepository adRepository){
        this.adRepository = adRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        AD ad = adRepository.findByAdEmail(username).orElseThrow(()-> new UsernameNotFoundException("Ad does not exists"));
        
        return UserDetailsImpl.build(ad);
    }
}
