package com.infirmary.backend.shared.utility;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

@Configuration
public class AppointmentQueueManager {
    private static final Queue<UUID> appointmentQueue = new LinkedList<>();
    private static final Queue<UUID> appointedQueue = new LinkedList<>();

    // Appointment Queue
    public static void addAppointmentToQueue(UUID appointmentId) {
        appointmentQueue.add(appointmentId);
    }

    public static UUID getNextAppointment() {
        return appointmentQueue.poll();
    }

    public static void removeElement(UUID id){
        appointmentQueue.remove(id);
    }

    public static boolean hasMoreAppointments() {
        return !appointmentQueue.isEmpty();
    }

    public static ArrayList<UUID> getQueue(){
        return new ArrayList<>(appointmentQueue);
    }

    //Appointed Queue
    public static void addAppointedQueue(UUID appointedId){
        appointedQueue.add(appointedId);
    }

    public static ArrayList<UUID> getAppointedQueue(){
        return new ArrayList<>(appointedQueue);
    }

    public static void removeApptEl(UUID aptId){
        appointedQueue.remove(aptId);
    }
  
    public static int getQueueSize() { return appointmentQueue.size(); }

    public static int getAptSize() {return appointedQueue.size();}

}
