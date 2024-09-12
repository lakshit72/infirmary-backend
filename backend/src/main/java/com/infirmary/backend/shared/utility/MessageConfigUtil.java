package com.infirmary.backend.shared.utility;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:message.properties")
@Getter
public class MessageConfigUtil {
    @Value("${patient_not_found}")
    private String patientNotFound;

    @Value("${invalid_data_exception}")
    private String invalidDataException;

    @Value("${sapid_exist_exception}")
    private String sapIdExistException;

    @Value("${medical_details_not_found_exception}")
    private String medicalDetailsNotFoundException;

    @Value("${appointment_not_found_exception}")
    private String appointmentNotFoundException;

    @Value("${doctor_not_found_exception}")
    private String doctorNotFoundException;

    @Value("${roles_not_found_exception}")
    private String rolesNotFoundException;

    @Value("${current_appointment_not_found}")
    private String currentAppointmentNotFound;

    @Value("${stock_not_found}")
    private String stockNotFound;

    @Value("${stock_already_exists}")
    private String stockAlreadyExists;

    @Value("${prescription_not_found_exception}")
    private String prescriptionNotFoundException;
}
