package com.infirmary.backend.shared.utility;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import java.util.Objects;

@Component
public class FunctionUtil {
    private static final Long MIN_NAME_LENGTH = 2L;

    public static <T> ResponseEntity<?> createSuccessResponse(T data, HttpHeaders... header) {
        if (header.length > 0) {
            return new ResponseEntity<>(data, header[0], HttpStatus.OK);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public static boolean isNameInvalid(String name) {
        return Objects.isNull(name)
                || name.length() <= MIN_NAME_LENGTH
                || StringUtils.containsAny(name,"[!@#$%&*()+=|<>?{}\\[\\]~]");
    }

    public static boolean isValidId(String Email){
        return Email.contains("stu.upes.ac.in") || Email.contains("ddn.upes.ac.in");
    }
}
