package com.infirmary.backend.configuration.recovery;

import java.util.List;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecoveryComponent {
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    
    @PostConstruct
    public void recover(){
        List<CurrentAppointment> cursAPt = currentAppointmentRepository.findAllByAppointmentNotNullAndDoctorIsNull();

        for(CurrentAppointment crs:cursAPt){
            if(crs.getAppointment().getDate() != LocalDate.now()){
                Appointment apt = crs.getAppointment();
                crs.setAppointment(null);
                currentAppointmentRepository.save(crs);
                appointmentRepository.delete(apt);
            }else{
                if(crs.getAppointment().getDoctor() == null) AppointmentQueueManager.addAppointmentToQueue(crs.getAppointment().getAppointmentId());
                if(crs.getAppointment().getDoctor() != null) AppointmentQueueManager.addAppointedQueue(crs.getAppointment().getAppointmentId());
            }
        }
    }
}
