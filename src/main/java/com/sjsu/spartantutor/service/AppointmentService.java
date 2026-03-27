package com.sjsu.spartantutor.service;

import com.sjsu.spartantutor.model.Appointment;
import com.sjsu.spartantutor.model.TimeSlot;
import com.sjsu.spartantutor.repository.AppointmentRepository;
import com.sjsu.spartantutor.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepo;
    private final TimeSlotRepository slotRepo;

    public AppointmentService(AppointmentRepository appointmentRepo, TimeSlotRepository slotRepo) {
        this.appointmentRepo = appointmentRepo;
        this.slotRepo = slotRepo;
    }

    public synchronized Appointment book(Long slotId, String studentName, String studentId, String notes) {
        TimeSlot slot = slotRepo.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));

        if (!"available".equals(slot.getStatus())) {
            log.warn("Slot {} is already booked", slotId);
            throw new IllegalStateException("Slot is no longer available");
        }

        Appointment appt = new Appointment();
        appt.setSlotId(slotId);
        appt.setTutorName(slot.getTutorName());
        appt.setSubject(slot.getSubject());
        appt.setDate(slot.getDate());
        appt.setTime(slot.getTime());
        appt.setDurationMinutes(slot.getDurationMinutes());
        appt.setStudentName(studentName);
        appt.setStudentId(studentId);
        appt.setNotes(notes);

        slotRepo.markBooked(slotId);
        return appointmentRepo.save(appt);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getById(String appointmentId) {
        return appointmentRepo.findById(appointmentId);
    }
}
