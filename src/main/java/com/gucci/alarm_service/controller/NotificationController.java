package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.service.AuthServiceHelper;
import com.gucci.alarm_service.service.NotificationEventHandler;
import com.gucci.alarm_service.service.NotificationReadService;
import com.gucci.alarm_service.service.NotificationWriteService;
import com.gucci.common.response.ApiResponse;
import com.gucci.common.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final AuthServiceHelper authServiceHelper;

    // 유저 정보 추출 테스트
    @GetMapping("/check")
    public ApiResponse<String> test(Authentication authentication) {
        Long userId = authServiceHelper.getCurrentUserId(authentication);
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
    public ApiResponse<Boolean> unreadAlarmExist(Authentication authentication) {
        Long receiverId = authServiceHelper.getCurrentUserId(authentication);
        boolean hasUnread = notificationReadService.existUnreadAlarm(receiverId);
        return ApiResponse.success(hasUnread);
    }

    // 전체 알림 조회
    @GetMapping
    public ApiResponse<Page<NotificationResponse>> getAllAlarms(Authentication authentication,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "20") int size) {
        Long receiverId = authServiceHelper.getCurrentUserId(authentication);
        Page<NotificationResponse> allAlarms = notificationReadService.getAllAlarms(receiverId, page, size);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, allAlarms);
    }

    // 안 읽은 알림 조회
    @GetMapping("/unread")
    public ApiResponse<Page<NotificationResponse>> unreadAlarmList(Authentication authentication,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "20") int size) {
        Long receiverId = authServiceHelper.getCurrentUserId(authentication);
        Page<NotificationResponse> unreadAlarms = notificationReadService.getUnreadAlarms(receiverId, page, size);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, unreadAlarms);
    }

    // 읽은 알림 조회
    @GetMapping("/read")
    public ApiResponse<Page<NotificationResponse>> readAlarmList(Authentication authentication,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int size) {
        Long receiverId = authServiceHelper.getCurrentUserId(authentication);
        Page<NotificationResponse> readAlarms = notificationReadService.getReadAlarms(receiverId, page, size);

        return ApiResponse.success(SuccessCode.DATA_FETCHED, readAlarms);
    }

    // 알림 읽음 처리
    @PatchMapping("/{id}/read")
    public ApiResponse<Void> markRead(@PathVariable Long id) {
        notificationWriteService.markRead(id);

        return ApiResponse.success();
    }

    // 전체 알림 읽음 처리
    @PatchMapping("/read/all")
    public ApiResponse<Void> markReadAll(Authentication authentication) {
        Long receiverId = authServiceHelper.getCurrentUserId(authentication);
        notificationWriteService.markReadAll(receiverId);

        return ApiResponse.success();
    }

    // 전체 알림 삭제
    @DeleteMapping
    public ApiResponse<Void> deleteAllAlarms(Authentication authentication) {
        Long receiverId = authServiceHelper.getCurrentUserId(authentication);
        notificationWriteService.deleteAll(receiverId);

        return ApiResponse.success();
    }

    // 특정 알림 삭제
    @DeleteMapping("{id}")
    public ApiResponse<Void> deleteAlarm(Authentication authentication,
                                         @PathVariable Long id) {
        Long userId = authServiceHelper.getCurrentUserId(authentication);
        notificationWriteService.deleteAlarm(userId, id);
        return ApiResponse.success();
    }

}
