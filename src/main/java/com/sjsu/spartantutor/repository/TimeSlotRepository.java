package com.sjsu.spartantutor.repository;

import com.sjsu.spartantutor.model.TimeSlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLDataException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class TimeSlotRepository {

    private static final Logger log = LoggerFactory.getLogger(TimeSlotRepository.class);

    private final JdbcTemplate jdbc;

    public TimeSlotRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<TimeSlot> findAvailable() {
        String sql = "SELECT * FROM time_slot WHERE status = 'available'";
        return jdbc.query(sql, new TimeSlotRowMapper());
    }

    public Optional<TimeSlot> findById(Long slotId) {
        String sql = "SELECT * FROM time_slot WHERE slot_id = ?";
        List<TimeSlot> results = jdbc.query(sql, new TimeSlotRowMapper(), slotId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<TimeSlot> findByIdWithLock(Long slotId) {
        String sql = "SELECT * FROM time_slot WHERE slot_id = ? FOR UPDATE";
        log.info("Acquiring lock on slot {}", slotId);
        List<TimeSlot> results = jdbc.query(sql, new TimeSlotRowMapper(), slotId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void markBooked(Long slotId) {
        String sql = "UPDATE time_slot SET status = 'booked' WHERE slot_id = ?";
        jdbc.update(sql, slotId);
        log.info("Slot {} marked as booked", slotId);
    }

    private static class TimeSlotRowMapper implements RowMapper<TimeSlot> {
        @Override
        public TimeSlot mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TimeSlot(
                    rs.getLong("slot_id"),
                    rs.getString("tutor_name"),
                    rs.getString("subject"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getInt("duration_minutes")
            );

        }
    }

    public void markAvailable(Long slotId) {
        String sql = "UPDATE time_slot SET status = 'available' WHERE slot_id = ?";
        jdbc.update(sql, slotId);
        log.info("Slot {} marked as available", slotId);
    }

}
