package com.infirmary.backend.configuration.model;

import com.infirmary.backend.configuration.dto.AppointmentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "appointment")
@NoArgsConstructor
public class Appointment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sap_email", referencedColumnName = "sap_email", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_email", referencedColumnName = "doctor_email", nullable = false)
    private Doctor doctor;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prescription_id", referencedColumnName = "prescription_id")
    private Prescription prescription;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_follow_up", columnDefinition = "boolean default false")
    private Boolean isFollowUp;

    @Column(name = "pref_doctor")
    private String prefDoctor;

    @Column(name = "reason_for_pref")
    private String reasonForPreference;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prev_appointment",referencedColumnName = "appointment_id",nullable = true)
    private Appointment prevAppointment;

    public Appointment(AppointmentDTO appointmentDTO) {
        this.appointmentId = appointmentDTO.getAppointmentId();
        this.patient = new Patient(appointmentDTO.getPatientDTO());
        this.doctor = new Doctor(appointmentDTO.getDoctorDTO());
        this.date = appointmentDTO.getDate();
        this.prescription = new Prescription(appointmentDTO.getPrescriptionDTO());
        this.reason = appointmentDTO.getReason();
        this.isFollowUp = appointmentDTO.getIsFollowUp();
        this.prefDoctor = appointmentDTO.getPreferredDoctor();
        this.reasonForPreference = appointmentDTO.getReasonPrefDoctor();
        this.prevAppointment = appointmentDTO.getPrevAppointment();
    }
}
