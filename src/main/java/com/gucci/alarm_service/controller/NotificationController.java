package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.service.NotificationService;
import com.gucci.common.response.ApiResponse;
import com.gucci.common.response.SuccessCode;
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

        return ApiResponse.success(SuccessCode.DATA_FETCHED, allAlarams);
    }

    // 안 읽은 알림 조회
    @GetMapping("/unread")
    public ApiResponse<List<NotificationResponse>> unreadAlarmList(
            @RequestHeader("X-USER-ID") Long receiverId) {
        List<NotificationResponse> unreadAlarms = notificationService.getUnreadAlarms(receiverId);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, unreadAlarms);
    }

    // 알림 읽음 처리
    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markRead(@PathVariable Long id) {
        notificationService.markRead(id);

        return ApiResponse.success();
    }
}
