package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import com.infirmary.backend.configuration.model.CurrentAppointment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrentAppointmentDTO {
    private UUID currentAppointmentId;
    private AppointmentDTO appointmentDTO;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;

    public CurrentAppointmentDTO(CurrentAppointment currentAppointment){
        this.currentAppointmentId = currentAppointment.getCurrentAppointmentId();
        this.appointmentDTO = new AppointmentDTO(currentAppointment.getAppointment());
        this.patientDTO = new PatientDTO(currentAppointment.getPatient());
        this.doctorDTO = new DoctorDTO(currentAppointment.getDoctor());
    }
}
