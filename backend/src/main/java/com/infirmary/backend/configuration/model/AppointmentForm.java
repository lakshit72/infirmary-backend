package com.infirmary.backend.configuration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import com.infirmary.backend.configuration.dto.AppointmentReqDTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "appointment_form")
public class AppointmentForm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_follow_up", columnDefinition = "boolean default false")
    private Boolean isFollowUp;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prev_appointment",nullable = true)
    private Appointment prevAppointment;

    @ManyToOne(fetch = FetchType.EAGER)
    private Doctor prefDoctor;

    @Column(name = "reason_for_pref")
    private String reasonForPreference;

    public AppointmentForm(AppointmentReqDTO appointmentReqDTO){
        this.isFollowUp = appointmentReqDTO.getIsFollowUp();
        this.reason = appointmentReqDTO.getReason();
        this.reasonForPreference = appointmentReqDTO.getReasonPrefDoctor();
    }

}

