package com.infirmary.backend.shared.utility;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infirmary.backend.configuration.dto.DeleteAptSave;
import com.infirmary.backend.configuration.model.Appointment;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Objects;

@Component
public class FunctionUtil {
    private static final Long MIN_NAME_LENGTH = 2L;
    
    private static Integer radiusEarth = 6371000;

    private static Integer radiusAllow = 200;

    private static final String FILE_PATH = "./src/main/resources/Deleted/deleted.json";

    public static <T> ResponseEntity<?> createSuccessResponse(T data, HttpHeaders... header) {
        if (header.length > 0) {
            return new ResponseEntity<>(data, header[0], HttpStatus.OK);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public static void saveToJSON(Appointment appointment) throws IOException{
        // Create ObjectMapper and register JavaTimeModule for Java 8 date/time support
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        DeleteAptSave object = new DeleteAptSave(appointment);

        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            // If the file doesn't exist or is empty, create a new JSON array
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write("[".getBytes()); // Start the array
                mapper.writeValue(fos, object); // Write the object
            }
        } else {
            // If the file exists and is non-empty
            try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
                 FileChannel channel = raf.getChannel()) {

                // Move to the position before the last bracket
                long fileLength = file.length();
                channel.truncate(fileLength - 1);

                // Add a comma separator
                try (FileOutputStream fos = new FileOutputStream(file, true)) {
                    fos.write(",".getBytes());
                    mapper.writeValue(fos, object); // Append the new object
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file,true)) {
            fos.write("]".getBytes()); // Close the array
            fos.close();
        }

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

    public static boolean isValidPassword(String str) {
        // Check if the string has at least 8 characters
        if (str.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;

        // Check each character
        for (char c : str.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }

            // If all conditions are met, return true early
            if (hasUppercase && hasLowercase && hasNumber && hasSpecialChar) {
                return true;
            }
        }

        // Check if all conditions are met
        return hasUppercase && hasLowercase && hasNumber && hasSpecialChar;
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
