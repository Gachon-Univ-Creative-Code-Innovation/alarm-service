package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.service.NotificationService;
import com.gucci.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm-service/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    // 알림 연결(구독)
    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationService.subscribe(userId);
    }

    // 알림 전송
    @PostMapping("/send")
    public ApiResponse<NotificationResponse> alarmCreate(@RequestBody NotificationRequest notificationRequest) {
        NotificationResponse save = notificationService.save(notificationRequest);
        return ApiResponse.success(save);
    }

    // 전체 알림 조회
    @GetMapping
    public ApiResponse<List<NotificationResponse>> getAllAlarms(
            @RequestHeader("X-USER-ID") Long receiverId) { // 로그인된 사용자 ID라고 가정
        List<NotificationResponse> allAlarams = notificationService.getAllAlarams(receiverId);

        return ApiResponse.success(allAlarams);
    }

}
