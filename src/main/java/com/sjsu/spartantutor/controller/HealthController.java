package com.sjsu.spartantutor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {

    private static final Logger log = LoggerFactory.getLogger(HealthController.class);

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("Health check requested");

        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "components", Map.of(
                        "db", Map.of("status", "UP"),
                        "diskSpace", Map.of("status", "UP"),
                        "notificationService", Map.of("status", "UP")
                )
        ));
    }
}