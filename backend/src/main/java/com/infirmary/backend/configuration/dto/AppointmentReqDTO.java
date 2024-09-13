package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.AppointmentForm;
import com.infirmary.backend.configuration.model.Doctor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppointmentReqDTO {
    private String reason;
    private Boolean isFollowUp;
    private Doctor preferredDoctor;
    private String reasonPrefDoctor;

    public AppointmentReqDTO(AppointmentForm appointmentForm) {
        this.reason = appointmentForm.getReason();
        this.isFollowUp = appointmentForm.getIsFollowUp();
        this.preferredDoctor = appointmentForm.getPrefDoctor();
        this.reasonPrefDoctor = appointmentForm.getReasonForPreference();
    }
}
