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
import java.util.concurrent.locks.ReentrantLock;

@Repository
public class TimeSlotRepository {

    private static final Logger log = LoggerFactory.getLogger(TimeSlotRepository.class);

    private final Map<Long, TimeSlot> store = new ConcurrentHashMap<>();
    private final Map<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

    public TimeSlotRepository() {
        store.put(1L, new TimeSlot(1L, "Khang Tran",  "CS146", "03/24/26", "2:30pm", 45));
        store.put(2L, new TimeSlot(2L, "Kaitlyn Tam", "CS146", "03/24/26", "2:30pm", 45));
        store.put(3L, new TimeSlot(3L, "Jacob Dinh",  "CS146", "03/24/26", "2:30pm", 45));
        store.keySet().forEach(id -> locks.put(id, new ReentrantLock()));
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

    public Optional<TimeSlot> findByIdWithLock(Long slotId) {
        ReentrantLock lock = locks.get(slotId);
        if (lock == null) return Optional.empty();

        boolean acquired = lock.tryLock();
        if (!acquired) {
            log.warn("Could not acquire lock on slot {}", slotId);
            throw new org.springframework.dao.CannotAcquireLockException("Slot " + slotId + " is locked");
        }

        log.info("Lock acquired on slot {}", slotId);
        return Optional.ofNullable(store.get(slotId));
    }

    public void markBooked(Long slotId) {
        TimeSlot slot = store.get(slotId);
        if (slot != null) {
            slot.setStatus("booked");
            log.info("Slot {} marked as booked", slotId);
        }
        ReentrantLock lock = locks.get(slotId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
            log.info("Lock released on slot {}", slotId);
        }
    }
    public void releaseLock(Long slotId) {
        ReentrantLock lock = locks.get(slotId);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

}
