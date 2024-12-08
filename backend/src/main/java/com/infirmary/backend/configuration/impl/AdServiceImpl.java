package com.infirmary.backend.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.infirmary.backend.configuration.dto.AdSubmitReqDTO;
import com.infirmary.backend.configuration.model.Appointment;
import com.infirmary.backend.configuration.model.AppointmentForm;
import com.infirmary.backend.configuration.model.CurrentAppointment;
import com.infirmary.backend.configuration.model.Doctor;
import com.infirmary.backend.configuration.model.Location;
import com.infirmary.backend.configuration.model.Prescription;
import com.infirmary.backend.configuration.model.PrescriptionMeds;
import com.infirmary.backend.configuration.model.Stock;
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

    public ResponseEntity<?> getPatientFormDetails(String sapEmail){
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

    @Override
    public String submitAppointment(AdSubmitReqDTO adSubmitReqDTO) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(adSubmitReqDTO.getPatEmail()).orElseThrow(() -> new ResourceNotFoundException("No appointment Found"));
        
        Doctor doctor = doctorRepository.findById(adSubmitReqDTO.getDoctorAss()).orElseThrow(() -> new ResourceNotFoundException("No Such doctor Exists"));

        if(!doctor.isStatus()) throw new IllegalArgumentException("Doctor Already Assigned");

        doctor.setStatus(false);
        doctorRepository.save(doctor);
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
                List<Long> delList = new ArrayList<>();
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

    @Override
    public String setDocStatus(Long docID, Boolean docStat,Double latitude, Double longitude) {
        Doctor doc = doctorRepository.findById(docID).orElseThrow(()->new ResourceNotFoundException("Doctor Not Found"));

        Optional<CurrentAppointment> currentAppointment = currentAppointmentRepository.findByDoctor(doc);

        if(currentAppointment.isPresent()){
            throw new IllegalArgumentException("an Appointment is Assigned");
        }

        if(docStat){
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

    @Override
    public String completeAppointment(String sapEmail) {
        CurrentAppointment currentAppointment = currentAppointmentRepository.findByPatient_Email(sapEmail).orElseThrow(()->new ResourceNotFoundException("No Appointment Scheduled"));

        if(currentAppointment.getAppointment() == null) throw new ResourceNotFoundException("No Appointment Scheduled");

        List<PrescriptionMeds> medLst = new ArrayList<>(currentAppointment.getAppointment().getPrescription().getMedicine());
        
        List<Stock> stocks = new ArrayList<>();

        for(PrescriptionMeds meds:medLst){
            Stock stock = stockRepository.findById(meds.getMedicine().getBatchNumber()).orElseThrow(()->new ResourceNotFoundException("No Such Medicine Exists"));

            if(stock.getQuantity() - (meds.getDuration()*meds.getDosage())<0) throw new IllegalArgumentException("Medicine Quantity Not Enough");

            stock.setQuantity(stock.getQuantity() - (meds.getDuration()*meds.getDosage()));

            stocks.add(stock);
        }

        stockRepository.saveAll(stocks);

        AppointmentQueueManager.removeApptEl(currentAppointment.getAppointment().getAppointmentId());
        
        currentAppointment.setAppointment(null);

        currentAppointmentRepository.save(currentAppointment);

        return "Appointment Completed";
    }

}
