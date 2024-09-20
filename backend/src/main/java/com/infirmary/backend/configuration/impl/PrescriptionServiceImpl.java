package com.infirmary.backend.configuration.impl;

import com.infirmary.backend.configuration.dto.PrescriptionDTO;
import com.infirmary.backend.configuration.dto.PrescriptionMedsDTO;
import com.infirmary.backend.configuration.dto.PrescriptionReq;
import com.infirmary.backend.configuration.dto.PrescriptionRes;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.model.PrescriptionMeds;
import com.infirmary.backend.configuration.model.Stock;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.PrescriptionMedsRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.PrescriptionService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final StockRepository stockRepository;
    private final DoctorRepository doctorRepository;
    private final PrescriptionMedsRepository prescriptionMedsRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, CurrentAppointmentRepository currentAppointmentRepository, AppointmentRepository appointmentRepository, StockRepository stockRepository, DoctorRepository doctorRepository, PrescriptionMedsRepository prescriptionMedsRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.stockRepository = stockRepository;
        this.doctorRepository = doctorRepository;
        this.prescriptionMedsRepository = prescriptionMedsRepository;
    }

    public void submitPrescription(PrescriptionReq prescriptionDTO) {
        // get Doc Email
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();

        //Get Assigned Appointment
        CurrentAppointment byDoctorEmail = currentAppointmentRepository.findByDoctor_DoctorEmail(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Appointment Scheduled"));
        
        Prescription prescription = new Prescription();

        //get Medicine
        List<PrescriptionMeds> meds = new ArrayList<>();
        
        for(PrescriptionMedsDTO pres:prescriptionDTO.getMeds()){
            PrescriptionMeds medicine = new PrescriptionMeds();
            medicine.setDosage(pres.getDosage());
            medicine.setDuration(pres.getDuration());
            Stock currMed = stockRepository.findById(pres.getMedicine()).orElseThrow(()->new ResourceNotFoundException("No Such Medicine"));
            if(currMed.getQuantity() < (pres.getDosage()*pres.getDuration())) throw new IllegalArgumentException("Not enough Stock available");
            currMed.setQuantity(currMed.getQuantity()-(pres.getDosage()*pres.getDuration()));
            Stock medStock = stockRepository.save(currMed);
            medicine.setMedicine(medStock);
            medicine.setSuggestion(pres.getSuggestion());
            meds.add(medicine);
        }
        meds = prescriptionMedsRepository.saveAll(meds);
        prescription.setMedicine(new HashSet<>(meds));
        prescription.setDiagnosis(prescriptionDTO.getDiagnosis());
        prescription.setDietaryRemarks(prescriptionDTO.getDietaryRemarks());
        prescription.setTestNeeded(prescriptionDTO.getTestNeeded());
        prescription.setPatient(byDoctorEmail.getPatient());
        prescription.setDoctor(byDoctorEmail.getDoctor());

        prescription = prescriptionRepository.save(prescription);

        // Set Prescription for appointment
        Appointment appointment = appointmentRepository.findByAppointmentId(byDoctorEmail.getAppointment().getAppointmentId());
        appointment.setPrescription(prescription);
        appointmentRepository.save(appointment);

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

    @Override
    public ResponseEntity<?> getPrescription(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No Appointment Scheduled"));

        PrescriptionRes prescriptionRes = new PrescriptionRes(new PrescriptionDTO(appointment.getPrescription()), appointment.getDate());

        return ResponseEntity.ok(prescriptionRes);
    }
}
