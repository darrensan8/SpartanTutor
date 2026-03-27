package com.sjsu.spartantutor.model;

public class TimeSlot {

    private Long slotId;
    private String tutorName;
    private String subject;
    private String date;      // e.g. "03/24/26"
    private String time;      // e.g. "2:30pm"
    private int durationMinutes;
    private String status;    // "available" or "booked"



    public TimeSlot(Long slotId, String tutorName, String subject, String date, String time, int durationMinutes) {
        this.slotId = slotId;
        this.tutorName = tutorName;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.durationMinutes = durationMinutes;
        this.status = "available";
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }

    public String getTutorName() { return tutorName; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
