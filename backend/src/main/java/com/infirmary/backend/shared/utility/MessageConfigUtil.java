package com.infirmary.backend.shared.utility;

import lombok.Getter;
import lombok.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:message.properties")
@Getter
public class MessageConfigUtil {
    private String patientNotFound;
}
