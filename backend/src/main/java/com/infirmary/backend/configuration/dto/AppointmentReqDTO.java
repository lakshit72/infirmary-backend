package com.infirmary.backend.configuration.dto;


import com.infirmary.backend.configuration.model.AppointmentForm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AppointmentReqDTO {
    @NotNull(message = "Please Provide reason for visit")
    @NotBlank(message = "Please Provide reason for visit")
    private String reason;
    private Boolean isFollowUp;
    private Long preferredDoctor;
    private String reasonPrefDoctor;

    public AppointmentReqDTO(AppointmentForm appointmentForm) {
        this.reason = appointmentForm.getReason();
        this.isFollowUp = appointmentForm.getIsFollowUp();
        this.reasonPrefDoctor = appointmentForm.getReasonForPreference();
    }
}
