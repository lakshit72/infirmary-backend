package com.infirmary.backend.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.repository.AppointmentFormRepository;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.service.ADService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements ADService{
    private final AppointmentRepository appointmentRepository;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentFormRepository appointmentFormRepository;
    private final PrescriptionRepository prescriptionRepository;
    
    public ResponseEntity<?> getQueue(){
        ArrayList<HashMap<String,String>> resp = new ArrayList<>();

        ArrayList<Long> idQueue = AppointmentQueueManager.getQueue();

        List<Appointment> apptList = appointmentRepository.findAllById(idQueue);

        
        for(Appointment apt:apptList){
            HashMap<String,String> dataMap = new HashMap<>();
            dataMap.put("name", apt.getPatient().getName());
            dataMap.put("sapEmail", apt.getPatient().getEmail());
            dataMap.put("reason", apt.getAptForm().getReason());
            dataMap.put("reason", apt.getAptForm().getReason());
            resp.add(dataMap);
        }

        return ResponseEntity.ok(resp);
    }

    public ResponseEntity<?> getPatientFormDetails(String sapEmail){
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(sapEmail).orElseThrow(() -> new ResourceNotFoundException("No Appointemnt Scheduled"));

        if(currentAppointment.getAppointment().equals(null)) throw new ResourceNotFoundException("No Appointment Scheduled");

        HashMap<String,Object> resp = new HashMap<>();
        currentAppointment.getAppointment().getAptForm().getPrefDoctor().setDoctorEmail("");
        currentAppointment.getAppointment().getAptForm().getPrefDoctor().setPassword("");
        resp.put("pref_doc", currentAppointment.getAppointment().getAptForm().getPrefDoctor());
        resp.put("doc_reason", currentAppointment.getAppointment().getAptForm().getReasonForPreference());
        resp.put("reason", currentAppointment.getAppointment().getAptForm().getReason());

        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> getCompletedQueue() {
        ArrayList<HashMap<String,Object>> resp = new ArrayList<>();

        ArrayList<Long> idQueue = AppointmentQueueManager.getAppointedQueue();

        List<Appointment> apptList = appointmentRepository.findAllById(idQueue);

        
        for(Appointment apt:apptList){
            HashMap<String,Object> dataMap = new HashMap<>();
            dataMap.put("name", apt.getPatient().getName());
            dataMap.put("sapEmail", apt.getPatient().getSapId());
            dataMap.put("DoctorId", apt.getDoctor().getDoctorId());
            dataMap.put("DoctorName", apt.getDoctor().getGender());
            dataMap.put("prescriptionId", apt.getPrescription().getPrescriptionId());
            resp.add(dataMap);
        }

        return ResponseEntity.ok(resp);
    }

    @Override
    public String submitAppointment(AdSubmitReqDTO adSubmitReqDTO) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(adSubmitReqDTO.getPatEmail()).orElseThrow(() -> new ResourceNotFoundException("No appointment Found"));
        
        Doctor doctor = doctorRepository.findById(adSubmitReqDTO.getDoctorAss()).orElseThrow(() -> new ResourceNotFoundException("No Such doctor Exists"));

        if(!doctor.isStatus()) throw new IllegalArgumentException("Doctor Already Assigned");

        doctor.setStatus(false);
        doctorRepository.save(doctor);
        currentAppointment.setDoctor(doctor);
        
        Appointment appointment = appointmentRepository.findByAppointmentId(currentAppointment.getAppointment().getAppointmentId());
        appointment.setDoctor(doctor);
        appointment.setWeight(adSubmitReqDTO.getWeight());
        appointment.setTemperature(adSubmitReqDTO.getTemperature());
        appointmentRepository.save(appointment);

        currentAppointmentRepository.save(currentAppointment);

        AppointmentQueueManager.removeElement(currentAppointment.getAppointment().getAppointmentId());

        return "Patient Assigned";
    }

    @Override
    public String rejectAppointment(String email) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(email).orElseThrow(() -> new ResourceNotFoundException("No Appointment Scheduled"));

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Apointment Scheduled");

        AppointmentQueueManager.removeElement(currentAppointment.getAppointment().getAppointmentId());

        if(currentAppointment.getDoctor() != null){
            Doctor doctor = doctorRepository.findById(currentAppointment.getDoctor().getDoctorId()).orElseThrow(()->new ResourceNotFoundException("No Such Doctor"));
            doctor.setStatus(true);
            doctorRepository.save(doctor);
        }
        currentAppointment.setDoctor(null);

        if(currentAppointment.getAppointment().getPrescription() != null) prescriptionRepository.deleteById(currentAppointment.getAppointment().getPrescription().getPrescriptionId());
           
        if(currentAppointment.getAppointment().getAptForm() != null) appointmentFormRepository.deleteById(currentAppointment.getAppointment().getAptForm().getId());
            
        Long aptId = currentAppointment.getAppointment().getAppointmentId();
        
        currentAppointment.setAppointment(null);

        appointmentRepository.deleteById(aptId);

        return "Patient Appointment Rejected";
    }

    @Override
    public String setDocStatus(Long docID, Boolean docStat) {
        Doctor doc = doctorRepository.findById(docID).orElseThrow(()->new ResourceNotFoundException("Doctor Not Found"));
        doc.setStatus(docStat);
        doctorRepository.save(doc);
        return "Status Changed";
    }

}
