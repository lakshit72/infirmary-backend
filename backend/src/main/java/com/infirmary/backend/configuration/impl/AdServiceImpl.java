package com.infirmary.backend.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.service.ADService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdServiceImpl implements ADService{
    private final AppointmentRepository appointmentRepository;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    
    public ResponseEntity<?> getQueue(){
        ArrayList<HashMap<String,String>> resp = new ArrayList<>();

        ArrayList<Long> idQueue = AppointmentQueueManager.getQueue();

        List<Appointment> apptList = appointmentRepository.findAllById(idQueue);

        
        for(Appointment apt:apptList){
            HashMap<String,String> dataMap = new HashMap<>();
            dataMap.put("name", apt.getPatient().getName());
            dataMap.put("sapEmail", apt.getPatient().getSapId());
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

}
