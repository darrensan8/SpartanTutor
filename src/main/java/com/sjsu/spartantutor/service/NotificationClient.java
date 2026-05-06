package com.sjsu.spartantutor.service;

import com.sjsu.spartantutor.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {

    private static final Logger log = LoggerFactory.getLogger(NotificationClient.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final String notificationUrl = "http://localhost:8080/api/notifications";

    public void sendBookingConfirmation(String recipient, String tutorName,
                                        String subject, String date, String time) {
        try {
            NotificationRequest request = new NotificationRequest(
                    "email",
                    recipient,
                    "Appointment Confirmed - SpartanTutor",
                    "Your appointment with " + tutorName + " for " + subject +
                            " on " + date + " at " + time + " is confirmed."
            );

            restTemplate.postForObject(notificationUrl + "/send", request, Object.class);
            log.info("Notification sent to {}", recipient);

        } catch (RestClientException e) {
            log.warn("Notification failed for {}. Booking still confirmed. Error={}", recipient, e.getMessage());
        }
    }
}