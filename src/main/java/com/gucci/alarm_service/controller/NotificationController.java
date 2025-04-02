package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping( "/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> alarmCreate(@RequestBody NotificationRequest notificationRequest) {
        NotificationResponse save = notificationService.save(notificationRequest);
        return ResponseEntity.ok().body(save);
    }
}
