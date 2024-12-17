package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.Appointment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentDTO {
    private Long appointmentId;
    private PatientDTO patientDTO;
    private DoctorDTO doctorDTO;
    private LocalDate date;
    private PrescriptionDTO prescriptionDTO;
    private Integer tokenNo;
    private Float weight;
    private Float temperature;

    public AppointmentDTO(Appointment appointment) {
        this.patientDTO = new PatientDTO(appointment.getPatient());
        this.doctorDTO = new DoctorDTO(appointment.getDoctor());
        this.date = appointment.getDate();
        this.prescriptionDTO = new PrescriptionDTO(appointment.getPrescription());
        this.tokenNo = appointment.getTokenNo();
        this.temperature = appointment.getTemperature();
        this.weight = appointment.getWeight();
    }
}
