package com.infirmary.backend.configuration.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

import com.infirmary.backend.configuration.dto.AppointmentReqDTO;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "appointment_form")
public class AppointmentForm implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "is_follow_up", columnDefinition = "boolean default false")
    private Boolean isFollowUp;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pref_doc",referencedColumnName = "doctor_id")
    private Doctor prefDoctor;

    @Column(name = "reason_for_pref")
    private String reasonForPreference;

    public AppointmentForm(AppointmentReqDTO appointmentReqDTO){
        this.isFollowUp = appointmentReqDTO.getIsFollowUp();
        this.reason = appointmentReqDTO.getReason();
        this.prefDoctor = appointmentReqDTO.getPreferredDoctor();
        this.reasonForPreference = appointmentReqDTO.getReasonPrefDoctor();
    }

}

