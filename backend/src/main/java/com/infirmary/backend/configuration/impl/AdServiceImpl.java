package com.infirmary.backend.configuration.impl;

import static com.infirmary.backend.shared.utility.FunctionUtil.createSuccessResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.model.AD;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.AppointmentForm;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.model.PrescriptionMeds;
import com.infirmary.backend.configuration.model.Stock;
import com.infirmary.backend.configuration.repository.AdRepository;
import com.infirmary.backend.configuration.repository.AppointmentFormRepository;
import com.infirmary.backend.configuration.repository.AppointmentRepository;
import com.infirmary.backend.configuration.repository.CurrentAppointmentRepository;
import com.infirmary.backend.configuration.repository.DoctorRepository;
import com.infirmary.backend.configuration.repository.LocationRepository;
import com.infirmary.backend.configuration.repository.PrescriptionMedsRepository;
import com.infirmary.backend.configuration.repository.PrescriptionRepository;
import com.infirmary.backend.configuration.repository.StockRepository;
import com.infirmary.backend.configuration.service.ADService;
import com.infirmary.backend.shared.utility.AppointmentQueueManager;
import com.infirmary.backend.shared.utility.FunctionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdServiceImpl implements ADService{
    private final AppointmentRepository appointmentRepository;
    private final CurrentAppointmentRepository currentAppointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentFormRepository appointmentFormRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final StockRepository stockRepository;
    private final PrescriptionMedsRepository prescriptionMedsRepository;
    private final LocationRepository locationRepository;
    private final AdRepository adRepository;
    

    //Get The queue pending appointment of doctor
    public ResponseEntity<?> getQueue(Double latitude,Double longitude){
        ArrayList<HashMap<String,String>> resp = new ArrayList<>();

        ArrayList<Long> idQueue = AppointmentQueueManager.getQueue();

        
        List<Location> locations = locationRepository.findAll();
        
        Location presentLocation = null;
        
        for(Location location:locations){
            if(FunctionUtil.IsWithinRadius(location.getLatitude(), location.getLongitude(), latitude, longitude)){
                presentLocation = location;
                break;
            }
        }
        
        if(presentLocation == null) throw new IllegalArgumentException("Be Available at the Infirmary to get the patient Queue");
        
        List<Appointment> apptList = appointmentRepository.findAllByAppointmentIdInAndLocation(idQueue,presentLocation);
        
        for(Appointment apt:apptList){
            HashMap<String,String> dataMap = new HashMap<>();
            dataMap.put("name", apt.getPatient().getName());
            dataMap.put("sapEmail", apt.getPatient().getEmail());
            dataMap.put("reason", apt.getAptForm().getReason());
            dataMap.put("aptId", apt.getAppointmentId().toString());
            dataMap.put("Id",apt.getPatient().getSapId());
            resp.add(dataMap);
        }

        return ResponseEntity.ok(resp);
    }


    // Get Patient Submitted Form of the Patient which they initially submit
    public ResponseEntity<?> getPatientFormDetails(String sapEmail){
        sapEmail = sapEmail.substring(0,sapEmail.indexOf("@")).concat(sapEmail.substring(sapEmail.indexOf("@")).replaceAll(",", "."));

        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(sapEmail).orElseThrow(() -> new ResourceNotFoundException("No Appointemnt Found"));

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Appointment Scheduled");

        HashMap<String,Object> resp = new HashMap<>();
        if(currentAppointment.getAppointment().getAptForm().getPrefDoctor() != null){
            currentAppointment.getAppointment().getAptForm().getPrefDoctor().setDoctorEmail("");
            currentAppointment.getAppointment().getAptForm().getPrefDoctor().setPassword("");
        }
        resp.put("pref_doc", currentAppointment.getAppointment().getAptForm().getPrefDoctor());
        resp.put("doc_reason", currentAppointment.getAppointment().getAptForm().getReasonForPreference());
        resp.put("reason", currentAppointment.getAppointment().getAptForm().getReason());

        return ResponseEntity.ok(resp);
    }


    //Get Queue of the Patient which have completed the doctor visit
    @Override
    public ResponseEntity<?> getCompletedQueue(Double latitude, Double longitude) {
        ArrayList<HashMap<String,Object>> resp = new ArrayList<>();

        ArrayList<Long> idQueue = AppointmentQueueManager.getAppointedQueue();
        List<Location> locations = locationRepository.findAll();
        
        Location presentLocation = null;
        
        for(Location location:locations){
            if(FunctionUtil.IsWithinRadius(location.getLatitude(), location.getLongitude(), latitude, longitude)){
                presentLocation = location;
                break;
            }
        }
        
        if(presentLocation == null) throw new IllegalArgumentException("Be Available at the Infirmary to get the patient Queue");
        
        List<Appointment> apptList = appointmentRepository.findAllByAppointmentIdInAndLocation(idQueue,presentLocation);
        
        for(Appointment apt:apptList){
            HashMap<String,Object> dataMap = new HashMap<>();
            dataMap.put("name", apt.getPatient().getName());
            dataMap.put("sapEmail", apt.getPatient().getEmail());
            dataMap.put("Id",apt.getPatient().getSapId());
            dataMap.put("reason",apt.getAptForm().getReason());
            dataMap.put("aptId", apt.getAppointmentId().toString());
            resp.add(dataMap);
        }

        return ResponseEntity.ok(resp);
    }


    //Submit appointment form and assign doctor
    @Override
    public String submitAppointment(AdSubmitReqDTO adSubmitReqDTO) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(adSubmitReqDTO.getPatEmail()).orElseThrow(() -> new ResourceNotFoundException("No appointment Found"));
        
        Doctor doctor = doctorRepository.findById(adSubmitReqDTO.getDoctorAss()).orElseThrow(() -> new ResourceNotFoundException("No Such doctor Exists"));

        if(!doctor.isStatus()) throw new IllegalArgumentException("Doctor Already Assigned");

        doctor.setStatus(false);
        doctor = doctorRepository.save(doctor);
        currentAppointment.setDoctor(doctor);
        
        Appointment appointment = appointmentRepository.findByAppointmentId(currentAppointment.getAppointment().getAppointmentId());
        appointment.setDoctor(doctor);
        appointment.setWeight(adSubmitReqDTO.getWeight());
        appointment.setTemperature(adSubmitReqDTO.getTemperature());
        appointmentRepository.save(appointment);

        currentAppointmentRepository.save(currentAppointment);

        AppointmentQueueManager.removeElement(currentAppointment.getAppointment().getAppointmentId());

        return "Patient Assigned";
    }

    //Reject appointment for a patient by AD
    @Override
    public String rejectAppointment(String email) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(email).orElseThrow(() -> new ResourceNotFoundException("No Appointment Scheduled"));

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Apointment Scheduled");

        Appointment appointment = appointmentRepository.findByAppointmentId(currentAppointment.getAppointment().getAppointmentId());

        if(currentAppointment.getAppointment().getDoctor() == null) AppointmentQueueManager.removeElement(currentAppointment.getAppointment().getAppointmentId());
        else AppointmentQueueManager.removeApptEl(currentAppointment.getAppointment().getAppointmentId());

        if(currentAppointment.getDoctor() != null){
            Doctor doctor = doctorRepository.findById(currentAppointment.getDoctor().getDoctorId()).orElseThrow(()->new ResourceNotFoundException("No Such Doctor"));
            doctor.setStatus(true);
            doctorRepository.save(doctor);
        }
        currentAppointment.setDoctor(null);

        if(currentAppointment.getAppointment().getPrescription() != null){
            Prescription prescription = prescriptionRepository.findById(currentAppointment.getAppointment().getPrescription().getPrescriptionId()).orElseThrow(()->new ResourceNotFoundException("No Prescription Found"));
            
            if(currentAppointment.getAppointment().getPrescription().getMedicine() != null){
                List<UUID> delList = new ArrayList<>();
                for(PrescriptionMeds med: currentAppointment.getAppointment().getPrescription().getMedicine()){
                    delList.add(med.getPresMedicineId());
                }
                prescription.setMedicine(null);
                prescriptionMedsRepository.deleteAll(prescriptionMedsRepository.findAllById(delList));
            }

            appointment.setPrescription(null);

            prescriptionRepository.delete(prescription);
            
        } 
        
        if(currentAppointment.getAppointment().getAptForm() != null){
            AppointmentForm appointmentForm = appointmentFormRepository.findById(currentAppointment.getAppointment().getAptForm().getId()).orElseThrow(()->new ResourceNotFoundException("No Appointment Form Found"));
            appointment.setAptForm(null);
            appointmentFormRepository.delete(appointmentForm);
        }
        currentAppointment.setAppointment(null);
        
        appointmentRepository.deleteById(appointment.getAppointmentId());

        currentAppointmentRepository.save(currentAppointment);
        return "Patient Appointment Rejected";
    }

    //Set Doctor status by AD
    @Override
    public String setDocStatus(Long docID, Boolean docStat,Double latitude, Double longitude) {
        Doctor doc = doctorRepository.findById(docID).orElseThrow(()->new ResourceNotFoundException("Doctor Not Found"));

        Optional<CurrentAppointment> currentAppointment = currentAppointmentRepository.findByDoctor(doc);

        if(currentAppointment.isPresent()){
            throw new IllegalArgumentException("an Appointment is Assigned");
        }

        if(docStat){

            if(longitude == null || latitude == null) throw new IllegalArgumentException("No Location Mentioned");

            List<Location> locations = locationRepository.findAll();
            Location presentLocation = null;

            for(Location location:locations){
                if(FunctionUtil.IsWithinRadius(location.getLatitude(), location.getLongitude(), latitude, longitude)){
                    presentLocation = location;
                    break;
                }
            }

            if(presentLocation == null) throw new IllegalArgumentException("Must be present on location to Check In");
            doc.setLocation(presentLocation);
        }else{
            doc.setLocation(null);
        }

        doc.setStatus(docStat);
        doctorRepository.save(doc);
        return "Status Changed";
    }

    //Complete a appointment for a patient
    @Override
    public String completeAppointment(String sapEmail) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Appointment Scheduled"));

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Appointment Scheduled");

        List<PrescriptionMeds> medLst = new ArrayList<>(currentAppointment.getAppointment().getPrescription().getMedicine());
        
        List<Stock> stocks = new ArrayList<>();

        for(PrescriptionMeds meds:medLst){
            Stock stock = stockRepository.findById(meds.getMedicine().getId()).orElseThrow(()->new ResourceNotFoundException("No Such Medicine Exists"));

            Integer medQty = (int) Math.ceil((meds.getDosageAfternoon()+meds.getDosageEvening()+meds.getDosageMorning()));

            if(stock.getQuantity() - (meds.getDuration()*medQty)<0) throw new IllegalArgumentException("Medicine Quantity Not Enough");

            stock.setQuantity(stock.getQuantity() - (meds.getDuration()*medQty));

            stocks.add(stock);
        }

        stockRepository.saveAll(stocks);

        AppointmentQueueManager.removeApptEl(currentAppointment.getAppointment().getAppointmentId());
        
        currentAppointment.setAppointment(null);

        currentAppointmentRepository.save(currentAppointment);

        return "Appointment Completed";
    }

    //Get All the doctors and tokens assigned
    @Override
    public ResponseEntity<?> getTokenData(String email) {
        AD ad = adRepository.findByAdEmail(email).orElseThrow(()->new ResourceNotFoundException("No Such Ad exists"));

        if(ad.getLocation() == null) throw new IllegalArgumentException("Must be present at Infirmary");

        List<CurrentAppointment> currAppointments = currentAppointmentRepository.findAllByAppointmentNotNullAndDoctorNotNullAndAppointment_Location(ad.getLocation());

        List<Map<String,String>> responseOut = new ArrayList<>();

        for(CurrentAppointment currentAppointment:currAppointments){
            Map<String,String> resp = new HashMap<>();
            resp.put("doctorName",currentAppointment.getDoctor().getName());
            resp.put("PatientToken", currentAppointment.getAppointment().getTokenNo().toString());
            responseOut.add(resp);
        }

        return createSuccessResponse(responseOut);
    }


    //Get Patient Assigned to AD
    @Override
    public List<?> getAssignedPatient(String sapEmail) {
        AD ad = adRepository.findByAdEmail(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Such Ad exists"));

        if(ad.getLocation() == null) throw new IllegalArgumentException("Must be present at Infirmary");

        List<CurrentAppointment> currAppointments = currentAppointmentRepository.findAllByAppointmentNotNullAndDoctorNotNullAndAppointment_Location(ad.getLocation());

        List<Map<String,String>> responseOut = new ArrayList<>();

        for(CurrentAppointment currentAppointment:currAppointments){
            Map<String,String> resp = new HashMap<>();
            resp.put("doctorName",currentAppointment.getDoctor().getName());
            resp.put("PatientToken", currentAppointment.getAppointment().getTokenNo().toString());
            resp.put("PatientName", currentAppointment.getPatient().getName());
            responseOut.add(resp);
        }

        return responseOut;
    }


    @Override
    public List<?> getAppointmentByDate(LocalDate date, String sapEmail) {
        AD ad = adRepository.findByAdEmail(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Such Ad exists"));

        if(ad.getLocation() == null) throw new IllegalArgumentException("Must be present at Infirmary");

        List<Appointment> allAppointments = appointmentRepository.findAllByDateAndLocationAndAppointmentIdNotIn(date, ad.getLocation(),AppointmentQueueManager.getAppointedQueue());

        List<Map<String,String>> resp = new ArrayList<>();

        for(Appointment curApt : allAppointments){
            Map<String,String> apt = new HashMap<>();
            apt.put("appointmentId",curApt.getAppointmentId().toString());
            apt.put("PatientName",curApt.getPatient().getName());
            apt.put("token", curApt.getTokenNo().toString());
            resp.add(apt);
        }

        return resp;
    }

}
