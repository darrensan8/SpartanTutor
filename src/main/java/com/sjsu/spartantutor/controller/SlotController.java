package com.sjsu.spartantutor.controller;

import com.sjsu.spartantutor.service.TimeSlotService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
