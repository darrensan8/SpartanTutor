CREATE TABLE IF NOT EXISTS time_slot (
                                         slot_id BIGSERIAL PRIMARY KEY,
                                         tutor_name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    date VARCHAR(20) NOT NULL,
    time VARCHAR(20) NOT NULL,
    duration_minutes INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'available'
    );

CREATE TABLE IF NOT EXISTS appointment (
                                           appointment_id VARCHAR(20) PRIMARY KEY,
    slot_id BIGINT NOT NULL,
    tutor_name VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    date VARCHAR(20) NOT NULL,
    time VARCHAR(20) NOT NULL,
    duration_minutes INT NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    student_id VARCHAR(50) NOT NULL,
    notes VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'confirmed',
    FOREIGN KEY (slot_id) REFERENCES time_slot(slot_id)
    );