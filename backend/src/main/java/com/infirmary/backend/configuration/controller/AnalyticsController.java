package com.infirmary.backend.configuration.controller;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infirmary.backend.configuration.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/geTotalPatient")
    public ResponseEntity<?> getTotalPatientCount(){
        return createSuccessResponse(analyticsService.getAllPatient());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getSchoolWise")
    public ResponseEntity<?> getSchoolWise(){
        return createSuccessResponse(analyticsService.getPatientSchoolWise());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getTopMeds")
    public ResponseEntity<?> getTopMeds(){
        return createSuccessResponse(analyticsService.getTopTenMeds());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getByResidenceType")
    public ResponseEntity<?> getAppointmentByResidenceType(){
        return createSuccessResponse(analyticsService.getByResidenceType());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getByDoctorName")
    public ResponseEntity<?> getAppointmentByDoctorName(){
        return createSuccessResponse(analyticsService.getByDoctor());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getMonthlyData")
    public ResponseEntity<?> getMonthlyData(){
        return createSuccessResponse(analyticsService.getMonthlyData());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getYearlyData")
    public ResponseEntity<?> getYearlyData(){
        return createSuccessResponse(analyticsService.getYearlyData());
    }

    @PreAuthorize("hasRole('ROLE_AD') or hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getDailyData")
    public ResponseEntity<?> getDailyData(){
        return createSuccessResponse(analyticsService.getDailyData());
    }
}
