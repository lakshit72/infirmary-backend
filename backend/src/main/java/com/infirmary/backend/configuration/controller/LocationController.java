package com.infirmary.backend.configuration.controller;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationRepository locationRepository;

    @GetMapping(value = "/")
    public ResponseEntity<?> getAllLocations(){
        return createSuccessResponse(locationRepository.findAll());
    }
}
