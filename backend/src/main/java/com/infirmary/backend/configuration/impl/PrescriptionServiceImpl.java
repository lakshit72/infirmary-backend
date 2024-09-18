package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.model.Stock;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.PrescriptionService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final StockRepository stockRepository;
    private final DoctorRepository doctorRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, CurrentAppointmentRepository currentAppointmentRepository, AppointmentRepository appointmentRepository, StockRepository stockRepository, DoctorRepository doctorRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.stockRepository = stockRepository;
        this.doctorRepository = doctorRepository;
    }

    public void submitPrescription(PrescriptionDTO prescriptionDTO) {
        // get Doc Email
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();

        //Get Assigned Appointment
        CurrentAppointment byDoctorEmail = currentAppointmentRepository.findByAppointment_Doctor_DoctorEmail(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Appointment Scheduled"));
        Prescription prescription = new Prescription(prescriptionDTO);

        //get Medicine
        List<Long> stock = prescriptionDTO.getStockIds();
        Set<Stock> stockList = new HashSet<>(stockRepository.findAllById(stock));
        Prescription saved = prescriptionRepository.save(prescription);
        
        // Set Prescription for appointment
        byDoctorEmail.getAppointment().setPrescription(saved);
        appointmentRepository.save(byDoctorEmail.getAppointment());

        //Free Doctor
        Doctor doctor = doctorRepository.findByDoctorEmail(sapEmail).orElseThrow(() -> new ResourceNotFoundException("No Doctor Found"));
        doctor.setStatus(true);

        // Add to Completed Queue
        AppointmentQueueManager.addAppointedQueue(byDoctorEmail.getAppointment().getAppointmentId());
        
        //Save Doctor Remove Doc from Current Appointment
        doctorRepository.save(doctor);
        byDoctorEmail.setDoctor(null);
        currentAppointmentRepository.save(byDoctorEmail);
    }
}
