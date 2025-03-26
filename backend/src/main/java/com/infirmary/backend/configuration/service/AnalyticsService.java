package com.infirmary.backend.configuration.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface AnalyticsService {
    Long getAllPatient();
    List<Object[]> getPatientSchoolWise();
    List<Object[]> getTopTenMeds();
    List<Object[]> getByResidenceType();
    List<Map<?,?>> getByDoctor();
    List<?> getMonthlyData();
    List<Object[]> getYearlyData();
    List<Object[]> getDailyData();
}
