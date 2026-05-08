package com.sjsu.spartantutor.repository;

import com.sjsu.spartantutor.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepository {

    private static final Logger log = LoggerFactory.getLogger(AppointmentRepository.class);

    private final JdbcTemplate jdbc;

    public AppointmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Appointment save(Appointment appointment) {
        Integer maxId = jdbc.queryForObject(
                "SELECT COALESCE(MAX(CAST(SUBSTRING(appointment_id, 5) AS INTEGER)), 100) FROM appointment",
                Integer.class
        );
        int nextId = (maxId != null ? maxId : 100) + 1;
        String id = "APMT" + nextId;
        appointment.setAppointmentId(id);
        appointment.setStatus("confirmed");

        String sql = """
                INSERT INTO appointment
                (appointment_id, slot_id, tutor_name, subject, date, time,
                 duration_minutes, student_name, student_id, notes, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbc.update(sql,
                appointment.getAppointmentId(),
                appointment.getSlotId(),
                appointment.getTutorName(),
                appointment.getSubject(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getDurationMinutes(),
                appointment.getStudentName(),
                appointment.getStudentId(),
                appointment.getNotes(),
                appointment.getStatus()
        );

        log.info("Appointment {} saved for student {}", id, appointment.getStudentName());
        return appointment;
    }

    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointment";
        return jdbc.query(sql, new AppointmentRowMapper());
    }

    public Optional<Appointment> findById(String appointmentId) {
        String sql = "SELECT * FROM appointment WHERE appointment_id = ?";
        List<Appointment> results = jdbc.query(sql, new AppointmentRowMapper(), appointmentId);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void cancel(String appointmentId) {
        String sql = "UPDATE appointment SET status = 'cancelled' WHERE appointment_id = ?";
        jdbc.update(sql, appointmentId);
        log.info("Appointment {} cancelled", appointmentId);
    }

    private static class AppointmentRowMapper implements RowMapper<Appointment> {
        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Appointment a = new Appointment();
            a.setAppointmentId(rs.getString("appointment_id"));
            a.setSlotId(rs.getLong("slot_id"));
            a.setTutorName(rs.getString("tutor_name"));
            a.setSubject(rs.getString("subject"));
            a.setDate(rs.getString("date"));
            a.setTime(rs.getString("time"));
            a.setDurationMinutes(rs.getInt("duration_minutes"));
            a.setStudentName(rs.getString("student_name"));
            a.setStudentId(rs.getString("student_id"));
            a.setNotes(rs.getString("notes"));
            a.setStatus(rs.getString("status"));
            return a;
        }
    }

    public List<Appointment> findConfirmed() {
        String sql = "SELECT * FROM appointment WHERE status = 'confirmed'";
        return jdbc.query(sql, new AppointmentRowMapper());
    }

    public List<Appointment> findCancelled() {
        String sql = "SELECT * FROM appointment WHERE status = 'cancelled'";
        return jdbc.query(sql, new AppointmentRowMapper());
    }

}
