package com.infirmary.backend.configuration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentResDTO {
    private Boolean isAppointedStatus;
    private Boolean isDoctorAppointed;

}
