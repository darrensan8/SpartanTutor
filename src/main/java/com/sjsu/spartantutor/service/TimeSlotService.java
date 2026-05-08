package com.sjsu.spartantutor.service;

import com.sjsu.spartantutor.model.TimeSlot;
import com.sjsu.spartantutor.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    private final TimeSlotRepository repo;

    public TimeSlotService(TimeSlotRepository repo) {
        this.repo = repo;
    }

    public List<TimeSlot> getAvailableSlots() {
        return repo.findAvailable();
    }

    public Optional<TimeSlot> getSlotById(Long slotId) {
        return repo.findById(slotId);
    }

    public void addSlot(String tutorName, String subject, String date, String time, int durationMinutes) {
        repo.addSlot(tutorName, subject, date, time, durationMinutes);
    }
}
