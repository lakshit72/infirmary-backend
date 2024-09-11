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
    private String prescriptionURL;
    private String reason;
    private Boolean isFollowUp;
    private Appointment prevAppointment;
    private String preferredDoctor;
    private String reasonPrefDoctor;

    public AppointmentDTO(Appointment appointment) {
        this.appointmentId = appointment.getAppointmentId();
        this.patientDTO = new PatientDTO(appointment.getPatient());
        this.doctorDTO = new DoctorDTO(appointment.getDoctor());
        this.date = appointment.getDate();
        this.prescriptionURL = appointment.getPrescriptionURL();
        this.reason = appointment.getReason();
        this.isFollowUp = appointment.getIsFollowUp();
        this.prevAppointment = appointment.getPrevAppointment();
        this.preferredDoctor = appointment.getPrefDoctor();
        this.reasonPrefDoctor = appointment.getReasonForPreference();
    }
}
