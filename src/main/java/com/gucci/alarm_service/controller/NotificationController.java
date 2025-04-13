package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.service.NotificationEventHandler;
import com.gucci.alarm_service.service.NotificationReadService;
import com.gucci.alarm_service.service.NotificationWriteService;
import com.gucci.common.response.ApiResponse;
import com.gucci.common.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm-service/notifications")
public class NotificationController {
    private final NotificationWriteService notificationWriteService;
    private final NotificationReadService notificationReadService;
    private final NotificationEventHandler notificationEventHandler;

    // 유저 정보 추출 테스트
    @GetMapping("/check")
    public ApiResponse<String> test(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.success("User Id " + userId);
    }

    // 알림 연결(구독) / 테스트용 (SSE 로직에 구현함)
    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        return notificationEventHandler.subscribe(userId);
    }

    // 알림 전송 / 테스트용 (SSE 로직에 구현함)
    @PostMapping("/send")
    public ApiResponse<NotificationResponse> alarmCreate(@RequestBody NotificationRequest notificationRequest) {
        NotificationResponse save = notificationWriteService.save(notificationRequest);
        return ApiResponse.success(save);
    }

    // 읽지 않은 알림 여부 / 테스트용 (SSE 로직에 구현함)
    @GetMapping("/unread/exists")
    public ApiResponse<Boolean> unreadAlarmExist(@RequestHeader("X-USER-ID") Long receiverId) {
        boolean hasUnread = notificationReadService.existUnreadAlarm(receiverId);
        return ApiResponse.success(hasUnread);
    }

    // 전체 알림 조회
    @GetMapping
    public ApiResponse<List<NotificationResponse>> getAllAlarms(
            @RequestHeader("X-USER-ID") Long receiverId) { // 로그인된 사용자 ID라고 가정
        List<NotificationResponse> allAlarms = notificationReadService.getAllAlarms(receiverId);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, allAlarms);
    }

    // 안 읽은 알림 조회
    @GetMapping("/unread")
    public ApiResponse<List<NotificationResponse>> unreadAlarmList(
            @RequestHeader("X-USER-ID") Long receiverId) {
        List<NotificationResponse> unreadAlarms = notificationReadService.getUnreadAlarms(receiverId);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, unreadAlarms);
    }

    // 알림 읽음 처리
    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markRead(@PathVariable Long id) {
        notificationWriteService.markRead(id);

        return ApiResponse.success();
    }

    // 전체 알림 읽음 처리
    @PatchMapping("/read/all")
    public ApiResponse<Void> markReadAll(@RequestHeader("X-USER-ID") Long receiverId) {
        notificationWriteService.markReadAll(receiverId);

        return ApiResponse.success();
    }

    // 전체 알림 삭제
    @DeleteMapping
    public ApiResponse<Void> deleteAllAlarms(@RequestHeader("X-USER-ID") Long receiverId) {
        notificationWriteService.deleteAll(receiverId);

        return ApiResponse.success();
    }

}
