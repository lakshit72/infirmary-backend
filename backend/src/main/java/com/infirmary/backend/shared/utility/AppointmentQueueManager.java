package com.infirmary.backend.shared.utility;

import com.infirmary.backend.configuration.dto.AppointmentResDTO;

import java.util.LinkedList;
import java.util.Queue;

public class AppointmentQueueManager {
    private final Queue<Long> appointmentQueue;

    public AppointmentQueueManager() {
        this.appointmentQueue = new LinkedList<>();
    }

    public void addAppointmentToQueue(Long appointmentId) {
        appointmentQueue.add(appointmentId);
    }

    public Long getNextAppointment() {
        return appointmentQueue.poll();
    }

    public boolean hasMoreAppointments() {
        return !appointmentQueue.isEmpty();
    }
}
