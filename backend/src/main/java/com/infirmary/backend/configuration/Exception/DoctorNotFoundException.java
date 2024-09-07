package com.infirmary.backend.configuration.Exception;

import javax.print.Doc;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(String message){super(message);}
}
