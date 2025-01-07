package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.PatientDetails;
import com.infirmary.backend.configuration.dto.PrescriptionReq;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.configuration.service.PatientService;
import com.infirmary.backend.configuration.service.PrescriptionService;
import com.infirmary.backend.configuration.service.StockService;

import jakarta.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.UUID;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping(value = "/api/doctor")
@Validated
public class DoctorController {
    private final DoctorService doctorService;
    private final PrescriptionService prescriptionService;
    private final StockService stockService;
    private final PatientService patientService;

    public DoctorController(DoctorService doctorService, PrescriptionService prescriptionService, StockService stockService, PatientService patientService) {
        this.doctorService = doctorService;
        this.prescriptionService = prescriptionService;
        this.stockService = stockService;
        this.patientService = patientService;
    }

    private static String getTokenClaims(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    //Not Used
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/byId")
    public ResponseEntity<?> getDoctorById() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        DoctorDTO response = doctorService.getDoctorById(id);
        return createSuccessResponse(response);
    }

    //Get Status of the doctor in doctor dashboard
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getStatus")
    public ResponseEntity<?> getDoctorStatusById() throws DoctorNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        Boolean doctorStatusById = doctorService.getDoctorStatusById(id);
        return createSuccessResponse(doctorStatusById);
    }

    //Set Doctor Status By Doctor
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/setStatus")
    public ResponseEntity<?> setDoctorStatus(@RequestParam("isDoctorCheckIn")
                                             Boolean isDoctorCheckIn, @RequestHeader(name = "X-Latitude",required = false) Double latitude, @RequestHeader(name = "X-Longitude", required = false) Double longitude) throws DoctorNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if((latitude == null || longitude == null) && isDoctorCheckIn){
            throw new IllegalArgumentException("Check In request require coordinates");
        }
        String id = userDetails.getUsername();
        Doctor doctorStatus = doctorService.setDoctorStatus(id, isDoctorCheckIn,latitude,longitude);
        return createSuccessResponse(doctorStatus.getName());
    }
    
    //Get Patient Stats for doctor
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/total-patient-count")
    public ResponseEntity<?> getAppointmentCountByDate()
            throws AppointmentNotFoundException {
        HashMap<String, Integer> countByDate = doctorService.getAppointmentCountByDate(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate());
        return createSuccessResponse(countByDate);
    }

    //Get Prescription for Current ID
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getPrescription/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable UUID id) {
        return prescriptionService.getPrescription(id);
    }
    
    //Not Used
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/prescription/{email}")
    public ResponseEntity<?> getPrescriptionByEmail(@PathVariable("email") String email)
    {
        HashMap<LocalDate, Prescription> prescriptionHistory = doctorService.getPrescriptionHistory(email);
        return createSuccessResponse(prescriptionHistory);
    }

    //Get Current Patient Assigned to Doctor
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getPatient")
    public ResponseEntity<?> getPatient(){
        PatientDetails patient = doctorService.getPatient(getTokenClaims());
        return ResponseEntity.ok(patient);
    }

    //Get Token of the Current Patient Assigned to the Doctor
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getCurrentToken")
    public ResponseEntity<?> getCurrentToken(){
        Integer tokenNo = doctorService.getCurrentTokenNo(getTokenClaims());
        return createSuccessResponse(tokenNo);
    }

    //Release the patient back to the queue of unassigned patient
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/releasePatient")
    public ResponseEntity<?> releasePatient(){
        return doctorService.releasePatient(getTokenClaims());
    }

    //Get Stock For AD
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/stock/")
    public ResponseEntity<?> getAllStock(){
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    //Add Stock for AD
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping(value = "/stock/addStock")
    public ResponseEntity<?> addStock(@Valid @RequestBody StockDTO stockDTO, BindingResult bindingResult, @RequestHeader(value = "X-Location",required = true) Long locId) throws StockAlreadyExists {
        StockDTO dto = stockService.addStock(stockDTO,locId);
        return createSuccessResponse(dto);
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping(value = "/stock/editStock")
    public ResponseEntity<?> editStock(@Valid @RequestBody StockDTO stockDTO, BindingResult bindingResult, @RequestHeader(value = "X-Location",required = true) Long locId) throws StockAlreadyExists {
        String dto = stockService.editStock(stockDTO,locId);
        return createSuccessResponse(dto);
    }

    //Delete Stock For AD
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping(value = "/stock/{stock}")
    public ResponseEntity<?> deleteStock(@PathVariable("stock") UUID stock) throws StockNotFoundException {
        stockService.deleteStock(stock);
        return createSuccessResponse("Stock deleted successfully!");
    }

    //Submit the prescription for the patient
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping(value = "/prescription/submit")
    public ResponseEntity<?> submitPrescription(@Valid @RequestBody PrescriptionReq prescriptionDTO,BindingResult bindingResult) {
        prescriptionService.submitPrescription(prescriptionDTO);
        return createSuccessResponse("Prescription submitted");
    }

    //Get Available Stock for Doctor
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/stock/available")
    public ResponseEntity<?> getAvailableStock(@RequestHeader(name = "X-Latitude",required = true) Double latitude, @RequestHeader(name = "X-Longitude", required = true) Double longitude){
        return ResponseEntity.ok(stockService.getAvailableStock(longitude,latitude));
    }

    //Get All Prescriptions of a patient
    @PostAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/getAppointmentPat/{id}")
    public ResponseEntity<?> getAppointmentDocs(@PathVariable String id) {
        return patientService.getAppointment(id);
    }

    //Export Excel
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping(value = "/export")
    public ResponseEntity<?> exportStocks() throws IOException {
        byte[] excelContent = stockService.exportStocksToExcel();

        ByteArrayResource resource = new ByteArrayResource(excelContent);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medicine_stocks.xlsx");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentLength(excelContent.length);

        return createSuccessResponse(resource, headers);
    }
}
