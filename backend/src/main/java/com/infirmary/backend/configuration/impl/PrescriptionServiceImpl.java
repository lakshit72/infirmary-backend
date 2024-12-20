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
import com.infirmary.backend.configuration.repository.MedicalDetailsRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.PrescriptionService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

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
    private final MedicalDetailsRepository medicalDetailsRepository;

    public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, CurrentAppointmentRepository currentAppointmentRepository, AppointmentRepository appointmentRepository, StockRepository stockRepository, DoctorRepository doctorRepository, MedicalDetailsRepository medicalDetailsRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.currentAppointmentRepository = currentAppointmentRepository;
        this.appointmentRepository = appointmentRepository;
        this.stockRepository = stockRepository;
        this.doctorRepository = doctorRepository;
        this.medicalDetailsRepository = medicalDetailsRepository;
    }

    public void submitPrescription(PrescriptionReq prescriptionDTO) {
        // get Doc Email
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String sapEmail = userDetails.getUsername();

        //Get Assigned Appointment
        CurrentAppointment byDoctorEmail = currentAppointmentRepository.findByDoctor_DoctorEmail(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Appointment Scheduled"));
        
        Prescription prescription = new Prescription();

        //Check Meds
        Map<UUID,Integer> instance = new HashMap<>();

        //set Medicine
        for(PrescriptionMedsDTO pres:prescriptionDTO.getMeds()){
            PrescriptionMeds medicine = new PrescriptionMeds();
            medicine.setDosageAfternoon(pres.getDosageAfternoon());
            medicine.setDosageEvening(pres.getDosageEvening());
            medicine.setDosageMorning(pres.getDosageMorning());
            medicine.setDuration(pres.getDuration());

            if(instance.get(pres.getMedicine()) != null) instance.put(pres.getMedicine(), instance.get(pres.getMedicine())+1);
            else instance.put(pres.getMedicine(), 1);

            if(pres.getDuration()<1) throw new IllegalArgumentException("Duration Should be atleast 1");

            Float medQty = pres.getDosageAfternoon()+pres.getDosageMorning()+pres.getDosageEvening();

            if(!(medQty>0)) throw new IllegalArgumentException("No medicine quantity defined");

            Stock currMed = stockRepository.findById(pres.getMedicine()).orElseThrow(()->new ResourceNotFoundException("No Such Medicine"));
            if(currMed.getQuantity() < (medQty*pres.getDuration())) throw new IllegalArgumentException("Not enough Stock available");
            if(currMed.getExpirationDate().isBefore(LocalDate.now())) throw new IllegalArgumentException("Medicines expired");
            medicine.setMedicine(currMed);
            medicine.setSuggestion(pres.getSuggestion());
            prescription.addPresMed(medicine);
        }

        //check for duplicates
        for(UUID inst:instance.keySet()){
            if(instance.get(inst)>1) throw new IllegalArgumentException("Must Assign only 1 instance of each medicine");
        }

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

        PrescriptionRes prescriptionRes = new PrescriptionRes();

        appointment.getPatient().setPassword("");

        prescriptionRes.setPrescription(new PrescriptionDTO(appointment.getPrescription()));

        prescriptionRes.setDate(appointment.getDate());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        Date date = new Date(appointment.getTimestamp());

        prescriptionRes.setTime(simpleDateFormat.format(date));
        
        prescriptionRes.setResidenceType(medicalDetailsRepository.findByPatient_Email(appointment.getPatient().getEmail()).orElseThrow(() -> new ResourceNotFoundException("no Medical Details Found")).getResidenceType());

        return ResponseEntity.ok(prescriptionRes);
    }
}
