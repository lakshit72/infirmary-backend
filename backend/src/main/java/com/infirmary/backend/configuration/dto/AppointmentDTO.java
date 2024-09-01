package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.Patient;
import com.infirmary.backend.configuration.model.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private PatientDTO patient;
    private DoctorDTO doctor;
    private LocalDate date;
    private URL prescriptionURL;

    public AppointmentDTO(Appointment appointment) {
        this.appointmentId = appointment.getAppointmentId();
        this.patient = new PatientDTO(appointment.getPatient());
        this.doctor = new DoctorDTO(appointment.getDoctor());
        this.date = appointment.getDate();
        this.prescriptionURL = appointment.getPrescriptionURL();
    }
}
