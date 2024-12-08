package com.infirmary.backend.shared.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Component
public class FunctionUtil {
    private static final Long MIN_NAME_LENGTH = 2L;
    
    private static Integer radiusEarth = 6371000;

    private static Integer radiusAllow = 200;

    public static <T> ResponseEntity<?> createSuccessResponse(T data, HttpHeaders... header) {
        if (header.length > 0) {
            return new ResponseEntity<>(data, header[0], HttpStatus.OK);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public static boolean isNameInvalid(String name) {
        return Objects.isNull(name)
                || name.length() <= MIN_NAME_LENGTH
                || StringUtils.containsAny(name, "[!@#$%&*()+=|<>?{}\\[\\]~]");
    }

    public static boolean isValidId(String email) {
        if (email.contains("stu.upes.ac.in")) {
            String[] parts = email.split("@");
            if (parts.length > 0) {
                String localPath = parts[0];
                return localPath.matches("5\\d{8}") && !localPath.matches(".*[a-zA-Z].*");
            }
        }

        if (email.contains("ddn.upes.ac.in")) {
            String[] parts = email.split("@");
            if (parts.length > 0) {
                String localPath = parts[0];
                return localPath.matches("4\\d{8}") || localPath.matches(".*[a-zA-Z].*");
            }
        }
        return false;
    }

    public static Boolean IsWithinRadius(Double loc_lat, Double loc_long, Double pat_lat, Double pat_long){
        Double loc_lat_rads = Math.toRadians(loc_lat);
        Double loc_long_rads = Math.toRadians(loc_long);

        Double pat_lat_rads = Math.toRadians(pat_lat);
        Double pat_long_rads = Math.toRadians(pat_long);

        Double dlat = pat_lat_rads - loc_lat_rads;
        Double dlong = pat_long_rads - loc_long_rads;

        Double a = (Math.pow(Math.sin(dlat/2),2) + Math.cos(loc_lat_rads) * Math.cos(pat_lat_rads) * Math.pow(Math.sin(dlong/2),2));
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        Double dist = radiusEarth * c;

        return dist<=radiusAllow;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
