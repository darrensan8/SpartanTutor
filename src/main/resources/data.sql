INSERT INTO time_slot (tutor_name, subject, date, time, duration_minutes, status)
SELECT 'Khang Tran', 'CS146', '03/24/26', '2:30pm', 45, 'available'
    WHERE NOT EXISTS (SELECT 1 FROM time_slot WHERE tutor_name = 'Khang Tran');

INSERT INTO time_slot (tutor_name, subject, date, time, duration_minutes, status)
SELECT 'Kaitlyn Tam', 'CS146', '03/24/26', '2:30pm', 45, 'available'
    WHERE NOT EXISTS (SELECT 1 FROM time_slot WHERE tutor_name = 'Kaitlyn Tam');

INSERT INTO time_slot (tutor_name, subject, date, time, duration_minutes, status)
SELECT 'Jacob Dinh', 'CS146', '03/24/26', '2:30pm', 45, 'available'
    WHERE NOT EXISTS (SELECT 1 FROM time_slot WHERE tutor_name = 'Jacob Dinh');

