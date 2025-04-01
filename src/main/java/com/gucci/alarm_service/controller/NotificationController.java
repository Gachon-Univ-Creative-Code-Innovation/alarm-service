package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Void> alarmCreate(Notification notification) {
        notificationService.save(notification);
        return ResponseEntity.ok().build();
    }
}
