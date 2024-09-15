package com.infirmary.backend.shared.utility;

import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

@Configuration
public class AppointmentQueueManager {
    private static final Queue<Long> appointmentQueue = new LinkedList<>();
    private static final Queue<Long> appointedQueue = new LinkedList<>();

    // Appointment Queue
    public static void addAppointmentToQueue(Long appointmentId) {
        appointmentQueue.add(appointmentId);
    }

    public static Long getNextAppointment() {
        return appointmentQueue.poll();
    }

    public static boolean hasMoreAppointments() {
        return !appointmentQueue.isEmpty();
    }

    public static ArrayList<Long> getQueue(){
        return new ArrayList<>(appointmentQueue);
    }

    //Appointed Queue
    public static void addAppointedQueue(Long appointedId){
        appointedQueue.add(appointedId);
    }

    public static ArrayList<Long> getAppointedQueue(){
        return new ArrayList<>(appointedQueue);
    }
  
    public static Long getQueueSize() { return (long) appointmentQueue.size(); }

}
