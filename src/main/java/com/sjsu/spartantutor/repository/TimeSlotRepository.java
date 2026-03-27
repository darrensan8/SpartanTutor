package com.sjsu.spartantutor.repository;

import com.sjsu.spartantutor.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TimeSlotRepository {

    private static final Logger log = LoggerFactory.getLogger(TimeSlotRepository.class);

    private final Map<Long, TimeSlot> store = new ConcurrentHashMap<>();

    public TimeSlotRepository() {
        store.put(1L, new TimeSlot(1L, "Khang Tran",  "CS146", "03/24/26", "2:30pm", 45));
        store.put(2L, new TimeSlot(2L, "Kaitlyn Tam", "CS146", "03/24/26", "2:30pm", 45));
        store.put(3L, new TimeSlot(3L, "Jacob Dinh",  "CS146", "03/24/26", "2:30pm", 45));
        log.info("TimeSlotRepository initialized with {} seed slots", store.size());
    }

    public List<TimeSlot> findAvailable() {
        List<TimeSlot> available = new ArrayList<>();
        for (TimeSlot s : store.values()) {
            if ("available".equals(s.getStatus())) {
                available.add(s);
            }
        }
        return available;
    }

    public Optional<TimeSlot> findById(Long slotId) {
        return Optional.ofNullable(store.get(slotId));
    }

    public void markBooked(Long slotId) {
        TimeSlot slot = store.get(slotId);
        if (slot != null) {
            slot.setStatus("booked");
            log.info("Slot {} marked as booked", slotId);
        }
    }
}
