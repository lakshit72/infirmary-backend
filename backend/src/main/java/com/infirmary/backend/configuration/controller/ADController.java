package com.infirmary.backend.configuration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.repository.AdRepository;

@RestController
@RequestMapping("/api/AD")
public class ADController {
    private AdRepository adRepository;

    public ADController(AdRepository adRepository){
        this.adRepository = adRepository;

    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping("/getQueue")
    public ResponseEntity<?> getQueue(){

        return null;
    }

}
