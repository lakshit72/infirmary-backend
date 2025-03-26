package com.infirmary.backend.configuration.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.PrescriptionMedsRepository;
import com.infirmary.backend.configuration.service.AnalyticsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService{
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionMedsRepository prescriptionMedsRepository;
    private final DoctorRepository doctorRepository;
    
    @Override
    public Long getAllPatient() {
        return appointmentRepository.countDistinctPatients();
    }

    @Override
    public List<Object[]> getPatientSchoolWise() {
        LocalDate lasDate = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
        lasDate = lasDate.minusDays(30);
        return appointmentRepository.countAppointmentsGroupedBySchoolNative(lasDate);
    }

    @Override
    public List<Object[]> getTopTenMeds() {
        LocalDate lasDate = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
        lasDate = lasDate.minusDays(30);
        
        return prescriptionMedsRepository.countPrescriptionMedsGroupByName(lasDate);
    }

    @Override
    public List<Object[]> getByResidenceType() {
        LocalDate lasDate = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
        lasDate = lasDate.minusDays(30);

        return appointmentRepository.countAppointmentsGroupedByResidenceType(lasDate);
    }

    @Override
    public List<Map<?,?>> getByDoctor() {
        LocalDate lasDate = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
        lasDate = lasDate.minusDays(30);

        List<Object[]> results = appointmentRepository.countAppointmentsGroupedByDoctor(lasDate);

        // Map doctor ID to name
        return results.stream().map(result -> {
            UUID doctorId = (UUID) result[0];
            long count = ((Number) result[1]).longValue();

            // Fetch doctor name
            String doctorName = doctorRepository.findById(doctorId)
                                 .map(Doctor::getName)
                                 .orElse("Unknown Doctor");

            // Build response map
            Map<String, Object> map = new HashMap<>();
            map.put("name", doctorName);
            map.put("patientCount", count);
            return map;
        }).collect(Collectors.toList());

    }

    @Override
    public List<?> getMonthlyData() {
        return appointmentRepository.countAppointmentByMonth();
    }

    @Override
    public List<Object[]> getYearlyData() {
        return appointmentRepository.countAppointmentByYear();
    }

    @Override
    public List<Object[]> getDailyData() {
        LocalDate lasDate = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate();
        lasDate = lasDate.minusDays(7);

        return appointmentRepository.countAppointmentsByDate(lasDate);
    }
    
}
