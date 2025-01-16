package com.infirmary.backend.configuration.controller;

import com.infirmary.backend.configuration.Exception.AppointmentNotFoundException;
import com.infirmary.backend.configuration.Exception.DoctorNotFoundException;
import com.infirmary.backend.configuration.Exception.StockAlreadyExists;
import com.infirmary.backend.configuration.Exception.StockNotFoundException;
import com.infirmary.backend.configuration.dto.AdHocSubmitDTO;
import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.dto.DoctorDTO;
import com.infirmary.backend.configuration.dto.ReassignPatientDTO;
import com.infirmary.backend.configuration.dto.StockDTO;
import com.infirmary.backend.configuration.service.ADService;
import com.infirmary.backend.configuration.service.DoctorService;
import com.infirmary.backend.configuration.service.PrescriptionService;
import com.infirmary.backend.configuration.service.StockService;

import jakarta.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

@RestController
@RequestMapping("/api/AD")
@Validated
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
        return createSuccessResponse(doctorService.getAppointmentCountByDate(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.of("Asia/Kolkata")).toLocalDate()));
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
    public ResponseEntity<?> getAppointment(@PathVariable UUID id) {
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

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAdHocByDate")
    public ResponseEntity<?> getAdHocByDate(@RequestParam(name = "date") LocalDate date){
        return createSuccessResponse(adService.getAdHocAppointment(date));
    }

    //Get Stock For AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/stock/")
    public ResponseEntity<?> getAllStock(){
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/stock/available")
    public ResponseEntity<?> getAvailableStock(@RequestHeader(name = "X-Latitude",required = true) Double latitude, @RequestHeader(name = "X-Longitude", required = true) Double longitude){
        return ResponseEntity.ok(stockService.getAvailableStock(longitude,latitude));
    }

    //Add Stock for AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/stock/addStock")
    public ResponseEntity<?> addStock(@Valid @RequestBody StockDTO stockDTO, BindingResult bindingResult, @RequestHeader(value = "X-Location",required = true) Long locId) throws StockAlreadyExists {
        StockDTO dto = stockService.addStock(stockDTO,locId);
        return createSuccessResponse(dto);
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/stock/editStock")
    public ResponseEntity<?> editStock(@Valid @RequestBody StockDTO stockDTO, BindingResult bindingResult, @RequestHeader(value = "X-Location",required = true) Long locId) throws StockAlreadyExists {
        String dto = stockService.editStock(stockDTO,locId);
        return createSuccessResponse(dto);
    }

    //Assign a doctor for the patient 
    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/submitAppointment")
    public ResponseEntity<?> submitAppointment(@Valid @RequestBody AdSubmitReqDTO adSubmitReqDTO){
        return ResponseEntity.ok(adService.submitAppointment(adSubmitReqDTO));
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/submit/adHoc")
    public ResponseEntity<?> submitAdHocAppointment(@Valid @RequestBody AdHocSubmitDTO adHocSubmitDTO){
        return createSuccessResponse(adService.submitAdHocAppointment(adHocSubmitDTO, getTokenClaims()));
    }

    //Get patient and Doctor assigned
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAssignedPatient")
    public ResponseEntity<?> getAssignedPatient(){
        return createSuccessResponse(adService.getAssignedPatient(getTokenClaims()));
    }

    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/getAppointmentByDate")
    public ResponseEntity<?> getAppointmentByDate(@RequestParam(value = "date") LocalDate date){
        return createSuccessResponse(adService.getAppointmentByDate(date, getTokenClaims()));
    }

    //Delete Stock For AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @DeleteMapping(value = "/stock/{stock}")
    public ResponseEntity<?> deleteStock(@PathVariable("stock") UUID stock) throws StockNotFoundException {
        stockService.deleteStock(stock);
        return createSuccessResponse("Stock deleted successfully!");
    }

    //Reject Appointment by AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/rejectAppointment")
    public ResponseEntity<?> rejectAppointment(@RequestParam("email") String email) throws IOException{
        return ResponseEntity.ok(adService.rejectAppointment(email));
    }

    //Set Doctor Status for AD
    @PreAuthorize("hasRole('ROLE_AD')")
    @GetMapping(value = "/setStatus/{docId}")
    public ResponseEntity<?> setDoctorStatusAD(@RequestParam("isDoctorCheckIn") Boolean isDoctorCheckIn,@PathVariable UUID docId,@RequestHeader(name = "X-Latitude",required = false) Double latitude, @RequestHeader(name = "X-Longitude", required = false) Double longitude) throws DoctorNotFoundException {
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

    //Export to excel
    @PreAuthorize("hasRole('ROLE_AD')")
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

    //Reassign Patient
    @PreAuthorize("hasRole('ROLE_AD')")
    @PostMapping(value = "/reassign")
    public ResponseEntity<?> reassignPatient(@Valid @RequestBody ReassignPatientDTO reassignPatientDTO){
        return createSuccessResponse(adService.reassignPatient(reassignPatientDTO));
    }

}
