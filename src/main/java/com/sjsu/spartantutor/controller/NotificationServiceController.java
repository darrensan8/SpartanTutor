package com.sjsu.spartantutor.controller;

import com.sjsu.spartantutor.model.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationServiceController {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceController.class);

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> send(@RequestBody NotificationRequest request) {
        log.info("Notification received. Type={}, Recipient={}, Subject={}",
                request.getType(), request.getRecipient(), request.getSubject());

        // Simulate processing
        log.info("Sending {} to {}: {}", request.getType(), request.getRecipient(), request.getMessage());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "notificationId", "NOTIF-" + System.currentTimeMillis(),
                "message", "Notification sent successfully"
        ));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}