package com.sjsu.spartantutor.service;

import com.sjsu.spartantutor.model.Appointment;
import com.sjsu.spartantutor.model.TimeSlot;
import com.sjsu.spartantutor.repository.AppointmentRepository;
import com.sjsu.spartantutor.repository.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingTransactionService {

    private static final Logger log = LoggerFactory.getLogger(BookingTransactionService.class);

    private final AppointmentRepository appointmentRepo;
    private final TimeSlotRepository slotRepo;
    private final NotificationClient notificationClient;

    public BookingTransactionService(AppointmentRepository appointmentRepo,
                                     TimeSlotRepository slotRepo,
                                     NotificationClient notificationClient) {
        this.appointmentRepo = appointmentRepo;
        this.slotRepo = slotRepo;
        this.notificationClient = notificationClient;

    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Appointment attemptBooking(Long slotId, String studentName,
                                      String studentId, String notes) {
        TimeSlot slot = slotRepo.findByIdWithLock(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));

        if (!"available".equals(slot.getStatus())) {
            log.warn("Slot {} is already booked", slotId);
            throw new IllegalStateException("Slot is no longer available");
        }

        Appointment appointment = new Appointment();
        appointment.setSlotId(slotId);
        appointment.setTutorName(slot.getTutorName());
        appointment.setSubject(slot.getSubject());
        appointment.setDate(slot.getDate());
        appointment.setTime(slot.getTime());
        appointment.setDurationMinutes(slot.getDurationMinutes());
        appointment.setStudentName(studentName);
        appointment.setStudentId(studentId);
        appointment.setNotes(notes);

        slotRepo.markBooked(slotId);
        Appointment saved = appointmentRepo.save(appointment);

        notificationClient.sendBookingConfirmation(
                studentId + "@sjsu.edu",
                slot.getTutorName(),
                slot.getSubject(),
                slot.getDate(),
                slot.getTime()
        );


        log.info("Booking created. AppointmentID={}, SlotID={}, Student={}",
                saved.getAppointmentId(), slotId, studentName);

        return saved;
    }
}