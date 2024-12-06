package com.infirmary.backend.configuration.recovery;

import java.util.List;
import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infirmary.backend.configuration.dto.LocationDataDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.LocationRepository;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecoveryComponent {
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final AppointmentRepository appointmentRepository;
    private final LocationRepository locationRepository;
    
    @PostConstruct
    public void recover(){
        List<CurrentAppointment> cursAPt = currentAppointmentRepository.findAllByAppointmentNotNullAndDoctorIsNull();

        for(CurrentAppointment crs:cursAPt){
            if(!crs.getAppointment().getDate().equals(LocalDate.now())){
                Appointment apt = crs.getAppointment();
                crs.setAppointment(null);
                currentAppointmentRepository.save(crs);
                appointmentRepository.delete(apt);
            }else{
                if(crs.getAppointment().getDoctor() == null) AppointmentQueueManager.addAppointmentToQueue(crs.getAppointment().getAppointmentId());
                if(crs.getAppointment().getDoctor() != null) AppointmentQueueManager.addAppointedQueue(crs.getAppointment().getAppointmentId());
            }
        }

        //Check if location table empty
        if(locationRepository.count() > 0) return;

        // Add Locations
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<LocationDataDTO>> typeReference = new TypeReference<List<LocationDataDTO>>(){};

        try{
            List<LocationDataDTO> locations = mapper.readValue(ResourceUtils.getFile("classpath:coords.json"), typeReference);
            
            for(LocationDataDTO location:locations){
                locationRepository.save(new Location(location));
            }

        }catch(IOException e){
            e.printStackTrace();

        }

    }
}
