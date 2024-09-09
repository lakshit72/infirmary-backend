package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.model.DoctorStatus;
import com.infirmary.backend.configuration.service.DoctorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/doctor")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping(value = "/byId/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable("id") Long id) {
        DoctorDTO response = doctorService.getDoctorById(id);
        return createSuccessResponse(response);
    }

    @GetMapping(value = "/status/{id}")
    public ResponseEntity<?> getDoctorStatusById(@PathVariable("id") Long id) throws DoctorNotFoundException {
        DoctorStatus doctorStatusById = doctorService.getDoctorStatusById(id);
        return createSuccessResponse(doctorStatusById);
    }

    @PostMapping(value = "/status/{id}")
    public ResponseEntity<?> setDoctorStatus(@PathVariable("id") Long id,
                                             @RequestParam("isDoctorCheckIn")
                                             Boolean isDoctorCheckIn) throws DoctorNotFoundException {
        DoctorStatus doctorStatus = doctorService.setDoctorStatus(id, isDoctorCheckIn);
        return createSuccessResponse(doctorStatus);
    }

    @GetMapping(value = "/total-patient-count")
    public ResponseEntity<?> getAppointmentCountByDate(@RequestParam("date")
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       LocalDate date)
            throws AppointmentNotFoundException {
        int count = doctorService.getAppointmentCountByDate(date);
        return createSuccessResponse(count);
    }
}
