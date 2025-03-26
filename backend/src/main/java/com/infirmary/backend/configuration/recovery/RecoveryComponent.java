package com.infirmary.backend.configuration.recovery;

import java.util.List;
import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infirmary.backend.configuration.dto.AdminDTO;
import com.infirmary.backend.configuration.dto.LocationDataDTO;
import com.infirmary.backend.configuration.model.Admin;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.repository.AdminRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.LocationRepository;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecoveryComponent {
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final LocationRepository locationRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void recover(){
        List<CurrentAppointment> cursAPt = currentAppointmentRepository.findAllByAppointmentNotNullAndDoctorIsNull();

        for(CurrentAppointment crs:cursAPt){
                if(crs.getAppointment().getDoctor() == null) AppointmentQueueManager.addAppointmentToQueue(crs.getAppointment().getAppointmentId());
                if(crs.getAppointment().getDoctor() != null) AppointmentQueueManager.addAppointedQueue(crs.getAppointment().getAppointmentId());
        }

        //Check if location table empty
        ObjectMapper mapper = new ObjectMapper();
        if(!(locationRepository.count() > 0)) {

            
            // Add Locations
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

        TypeReference<List<AdminDTO>> typeReference = new TypeReference<List<AdminDTO>>(){};

        try{
            List<AdminDTO> admins = mapper.readValue(ResourceUtils.getFile("classpath:admin.json"), typeReference);

            for(AdminDTO admin: admins){
                if(adminRepository.existsByAdminEmail(admin.getEmail())) continue;

                Admin adminEnter = new Admin();
                adminEnter.setAdminEmail(admin.getEmail());
                adminEnter.setName(admin.getName());
                adminEnter.setPassword(passwordEncoder.encode(admin.getPassword()));

                adminRepository.save(adminEnter);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
