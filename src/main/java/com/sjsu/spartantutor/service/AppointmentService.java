package com.sjsu.spartantutor.service;

import com.sjsu.spartantutor.model.Appointment;
import com.sjsu.spartantutor.repository.AppointmentRepository;
import com.sjsu.spartantutor.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private static final int MAX_RETRIES = 3;
    private static final int INITIAL_BACKOFF_MS = 100;

    private final AppointmentRepository appointmentRepo;
    private final TimeSlotRepository slotRepo;
    private final BookingTransactionService bookingTransactionService;

    public AppointmentService(AppointmentRepository appointmentRepo, TimeSlotRepository slotRepo, BookingTransactionService bookingTransactionService) {
        this.appointmentRepo = appointmentRepo;
        this.slotRepo = slotRepo;
        this.bookingTransactionService = bookingTransactionService;
    }

    public Appointment book(Long slotId, String studentName, String studentId, String notes) {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                return bookingTransactionService.attemptBooking(slotId, studentName, studentId, notes);
            } catch (CannotAcquireLockException e) {
                log.warn("Lock conflict on slot {}. Attempt {}/{}", slotId, attempt + 1, MAX_RETRIES);
                if (attempt == MAX_RETRIES - 1) {
                    log.error("Booking failed after {} retries. SlotID={}", MAX_RETRIES, slotId);
                    throw new IllegalStateException("System busy. Please try again.");
                }
                try {
                    Thread.sleep(INITIAL_BACKOFF_MS * (int) Math.pow(2, attempt));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Booking interrupted.");
                }
            }
        }
        throw new IllegalStateException("Booking could not be completed.");
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public Optional<Appointment> getById(String appointmentId) {
        return appointmentRepo.findById(appointmentId);
    }
}
