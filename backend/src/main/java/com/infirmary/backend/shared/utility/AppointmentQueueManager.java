package com.infirmary.backend.shared.utility;

import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.Queue;

@Configuration
public class AppointmentQueueManager {
    private static final Queue<Long> appointmentQueue = new LinkedList<>();

    public static void addAppointmentToQueue(Long appointmentId) {
        appointmentQueue.add(appointmentId);
    }

    public static Long getNextAppointment() {
        return appointmentQueue.poll();
    }

    public static boolean hasMoreAppointments() {
        return !appointmentQueue.isEmpty();
    }
}
