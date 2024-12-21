package com.infirmary.backend.configuration.dto;


import java.util.UUID;

import com.infirmary.backend.configuration.model.AppointmentForm;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AppointmentReqDTO {
    @NotBlank(message = "Please Provide reason for visit")
    private String reason;
    private Boolean isFollowUp;
    private UUID preferredDoctor;
    private String reasonPrefDoctor;

    public AppointmentReqDTO(AppointmentForm appointmentForm) {
        this.reason = appointmentForm.getReason();
        this.isFollowUp = appointmentForm.getIsFollowUp();
        this.reasonPrefDoctor = appointmentForm.getReasonForPreference();
    }
}
