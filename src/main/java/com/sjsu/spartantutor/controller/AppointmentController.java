package com.sjsu.spartantutor.controller;

import com.sjsu.spartantutor.model.Appointment;
import com.sjsu.spartantutor.model.TimeSlot;
import com.sjsu.spartantutor.service.AppointmentService;
import com.sjsu.spartantutor.service.TimeSlotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppointmentController {

    private static final Logger log = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;
    private final TimeSlotService slotService;

    public AppointmentController(AppointmentService appointmentService, TimeSlotService slotService) {
        this.appointmentService = appointmentService;
        this.slotService = slotService;
    }

    @GetMapping("/appointments")
    public String viewAppointments(Model model) {
        model.addAttribute("upcoming", appointmentService.getUpcomingAppointments());
        model.addAttribute("past", appointmentService.getPastAppointments());
        return "appointments";
    }

    @GetMapping("/book/{slotId}")
    public String bookForm(@PathVariable Long slotId, Model model) {
        TimeSlot slot = slotService.getSlotById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));
        model.addAttribute("slot", slot);
        return "book";
    }

    @PostMapping("/book")
    public String confirmBooking(@RequestParam Long slotId,
                                 @RequestParam String studentName,
                                 @RequestParam String studentId,
                                 @RequestParam(required = false) String notes,
                                 Model model) {
        log.info("Booking slot {} for student {}", slotId, studentName);
        Appointment appt = appointmentService.book(slotId, studentName, studentId, notes);
        model.addAttribute("appointment", appt);
        return "confirmation";
    }

    @PostMapping("/cancel/{appointmentId}")
    public String cancelAppointment(@PathVariable String appointmentId) {
        appointmentService.cancel(appointmentId);
        return "redirect:/appointments";
    }

}
