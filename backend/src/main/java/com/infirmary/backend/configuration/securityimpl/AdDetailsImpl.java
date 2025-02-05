package com.infirmary.backend.configuration.securityimpl;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.repository.AdRepository;


@Service
public class AdDetailsImpl implements UserDetailsService{
    private AdRepository adRepository;

    public AdDetailsImpl(AdRepository adRepository){
        this.adRepository = adRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException{
        AD ad = adRepository.findByAdEmail(username).orElseThrow(()-> new ResourceNotFoundException("Nursing Assistant does not exists"));
        return UserDetailsImpl.build(ad);
    }
}
