package com.gucci.alarm_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarm-service")
@RequiredArgsConstructor
public class HealthCheckController {
    private final Environment environment;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        "Alarm Service is running on port: "
                                + environment.getProperty("local.server.port")
                );
    }

    @GetMapping("/test")
    public  String test(){
        return "cicd test";

    }
}
