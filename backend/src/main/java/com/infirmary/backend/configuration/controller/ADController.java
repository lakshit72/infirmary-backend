package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.service.ADService;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.configuration.service.PrescriptionService;
import com.infirmary.backend.configuration.service.StockService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/AD")
public class ADController {
    private final DoctorService doctorService;
    private final ADService adService;
    private final PrescriptionService prescriptionService;
    private final StockService stockService;

    public ADController(ADService adService, DoctorService doctorService, PrescriptionService prescriptionService, StockService stockService){
        this.adService = adService;
        this.prescriptionService = prescriptionService;
        this.doctorService = doctorService;
        this.stockService = stockService;
    }

    private static String getTokenClaims(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = userDetails.getUsername();
        return id;
    }

    //Get Available doctors for AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAvailableDoctors")
    public ResponseEntity<?> getAllAvailableDoctors(@RequestHeader(name = "X-Latitude",required = true) Double latitude,@RequestHeader(name = "X-Longitude", required = true) Double longitude) throws DoctorNotFoundException {
        List<DoctorDTO> list = doctorService.getAvailableDoctors(latitude,longitude);
        return createSuccessResponse(list);
    }

    //Get Patient Statistics for AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/total-patient-count")
    public ResponseEntity<?> getPatientStat() throws AppointmentNotFoundException{
        return createSuccessResponse(doctorService.getAppointmentCountByDate(LocalDate.now()));
    }

    //Get Appointment Form for AD for Patient List
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAptForm/{sapEmail}")
    public ResponseEntity<?> getAptForm(@PathVariable String sapEmail){
        return adService.getPatientFormDetails(sapEmail);
    }

    //Get Prescription for Current ID
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getPrescription/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable Long id) {
        return prescriptionService.getPrescription(id);
    }

    //Get All doctors for AD to check-In-OUT
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAllDoctors")
    public ResponseEntity<?> getAllDoctors() throws DoctorNotFoundException {
        List<DoctorDTO> response = doctorService.getAllDoctors();
        return createSuccessResponse(response);
    }

    //Get Patient who are not assigned a doctor
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getPatientQueue")
    public ResponseEntity<?> getPatientQueue(@RequestHeader(name = "X-Latitude",required = true) Double latitude,@RequestHeader(name = "X-Longitude", required = true) Double longitude){
        return adService.getQueue(latitude,longitude);
    }

    //Get Patient Who have completed appointment from doctor
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getCompletedQueue")
    public ResponseEntity<?> getCompletedQueue(@RequestHeader(name = "X-Latitude",required = true) Double latitude,@RequestHeader(name = "X-Longitude", required = true) Double longitude){
        return adService.getCompletedQueue(latitude,longitude);
    }

    //Get Stock For AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/stock/")
    public ResponseEntity<?> getAllStock(){
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    //Add Stock for AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/stock/addStock")
    public ResponseEntity<?> addStock(@RequestBody StockDTO stockDTO, @RequestHeader(value = "X-Location",required = true) Long locId) throws StockAlreadyExists {
        StockDTO dto = stockService.addStock(stockDTO,locId);
        return createSuccessResponse(dto);
    }

    //Assign a doctor for the patient 
    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/submitAppointment")
    public ResponseEntity<?> submitAppointment(@RequestBody AdSubmitReqDTO adSubmitReqDTO){
        return ResponseEntity.ok(adService.submitAppointment(adSubmitReqDTO));
    }

    //Delete Stock For AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @DeleteMapping(value = "/stock/{batch-number}")
    public ResponseEntity<?> deleteStock(@PathVariable("batch-number") Long batchNumber) throws StockNotFoundException {
        stockService.deleteStock(batchNumber);
        return createSuccessResponse("Stock deleted successfully!");
    }

    //Reject Appointment by AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/rejectAppointment")
    public ResponseEntity<?> rejectAppointment(@RequestParam("email") String email){
        return ResponseEntity.ok(adService.rejectAppointment(email));
    }

    //Set Doctor Status for AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/setStatus/{docId}")
    public ResponseEntity<?> setDoctorStatusAD(@RequestParam("isDoctorCheckIn") Boolean isDoctorCheckIn,@PathVariable Long docId,@RequestHeader(name = "X-Latitude",required = false) Double latitude, @RequestHeader(name = "X-Longitude", required = false) Double longitude) throws DoctorNotFoundException {
        return createSuccessResponse(adService.setDocStatus(docId, isDoctorCheckIn,latitude,longitude));
    }

    //Complete the Appointment 
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/completeAppointment/{sapEmail}")
    public ResponseEntity<?> completeAppointment(@PathVariable String sapEmail){
        return ResponseEntity.ok(adService.completeAppointment(sapEmail));
    }

    //Get Token Data for the Token Screen
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getTokenData")
    public ResponseEntity<?> getTokenData(){
        return adService.getTokenData(getTokenClaims());
    }

}
