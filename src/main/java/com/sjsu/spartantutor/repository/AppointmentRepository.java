package com.sjsu.spartantutor.repository;

import com.sjsu.spartantutor.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AppointmentRepository {

    private static final Logger log = LoggerFactory.getLogger(AppointmentRepository.class);

    private final Map<String, Appointment> store = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(100);

    public Appointment save(Appointment appointment) {
        String id = "APMT" + counter.incrementAndGet();
        appointment.setAppointmentId(id);
        appointment.setStatus("confirmed");
        store.put(id, appointment);
        log.info("Appointment {} saved for student {}", id, appointment.getStudentName());
        return appointment;
    }

    public List<Appointment> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Appointment> findById(String appointmentId) {
        return Optional.ofNullable(store.get(appointmentId));
    }
}
