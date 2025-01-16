package com.infirmary.backend.configuration.dto;

import java.util.UUID;

import com.infirmary.backend.configuration.model.Appointment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAptSave {
    private String patEmail;
    private String docEmail;
    private String reason;
    private Long locId;
    private UUID prevAptId;
    private Long timestamp;
    
    public DeleteAptSave(Appointment appointment){
        this.patEmail = appointment.getPatient().getEmail();
        this.docEmail = appointment.getDoctor() != null ? appointment.getDoctor().getDoctorEmail():null;
        this.reason = appointment.getAptForm().getReason();
        this.locId = appointment.getLocation().getLocId();
        this.prevAptId = appointment.getAptForm().getIsFollowUp() ? appointment.getAptForm().getPrevAppointment().getAppointmentId():null;
        this.timestamp = appointment.getTimestamp();
    }

}
