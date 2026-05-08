package com.sjsu.spartantutor.controller;

import com.sjsu.spartantutor.service.TimeSlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SlotController {

    private final TimeSlotService slotService;

    public SlotController(TimeSlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping("/slots")
    public String viewSlots(Model model) {
        model.addAttribute("slots", slotService.getAvailableSlots());
        return "slots";
    }

    @GetMapping("/provider")
    public String providerPage(Model model) {
        model.addAttribute("slots", slotService.getAllSlots());
        return "provider";
    }

    @PostMapping("/provider/add")
    public String addSlot(@RequestParam String tutorName,
                          @RequestParam String subject,
                          @RequestParam String date,
                          @RequestParam String time,
                          @RequestParam int durationMinutes) {
        slotService.addSlot(tutorName, subject, date, time, durationMinutes);
        return "redirect:/slots";
    }
    @PostMapping("/provider/delete/{slotId}")
    public String deleteSlot(@PathVariable Long slotId) {
        slotService.deleteSlot(slotId);
        return "redirect:/provider";
    }
}
