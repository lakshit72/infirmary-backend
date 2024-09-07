package com.infirmary.backend.configuration.dto;

import com.infirmary.backend.configuration.model.AppointmentForm;
import com.infirmary.backend.configuration.service.AppointmentService;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AppointmentReqDTO {
    private String reason;
    private Boolean isFollowUp;
    private LocalDate prevAppointmentDate;
    private String preferredDoctor;
    private String reasonPrefDoctor;

    public AppointmentReqDTO(AppointmentForm appointmentForm, AppointmentService appointmentService) {
        this.reason = appointmentForm.getReason();
        this.isFollowUp = appointmentForm.getIsFollowUp();
        this.prevAppointmentDate = appointmentService.getLastAppointmentDateByEmail(
                appointmentForm.
                        getAppointment().
                        getPatient().
                        getEmail()
        );
        this.preferredDoctor = appointmentForm.getPrefDoctor();
        this.reasonPrefDoctor = appointmentForm.getReasonForPreference();
    }
}
