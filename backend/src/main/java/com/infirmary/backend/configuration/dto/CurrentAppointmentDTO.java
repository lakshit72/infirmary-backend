package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.CurrentAppointment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrentAppointmentDTO {

    private Long currentAppointmentId;
    private AppointmentDTO appointment;
    private PatientDTO patient;
    private DoctorDTO doctor;

    public CurrentAppointmentDTO(CurrentAppointment currentAppointment) {
        this.currentAppointmentId = currentAppointment.getCurrentAppointmentId();
        this.appointment = new AppointmentDTO(currentAppointment.getAppointment());
        this.patient = new PatientDTO(currentAppointment.getPatient());
        this.doctor = new DoctorDTO(currentAppointment.getDoctor());
    }
}
