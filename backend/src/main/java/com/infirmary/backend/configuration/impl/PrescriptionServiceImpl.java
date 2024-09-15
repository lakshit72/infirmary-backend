package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.service.PrescriptionService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

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

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, CurrentAppointmentRepository currentAppointmentRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public void submitPrescription(PrescriptionDTO prescriptionDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();
        CurrentAppointment byDoctorEmail = currentAppointmentRepository.findByAppointment_Doctor_DoctorEmail(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Appointment Scheduled"));
        Prescription prescription = new Prescription(prescriptionDTO);
        Prescription saved = prescriptionRepository.save(prescription);
        byDoctorEmail.getAppointment().setPrescription(saved);
        appointmentRepository.save(byDoctorEmail.getAppointment());
    }
}
