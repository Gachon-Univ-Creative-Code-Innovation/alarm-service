package com.gucci.alarm_service.service;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationReadService {
    private final NotificationRepository notificationRepository;

    // 알람 전체 조회
    public List<NotificationResponse> getAllAlarms(Long receiverId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    // 안 읽은 알람 조회
    public List<NotificationResponse> getUnreadAlarms(Long receiverId) {
        return notificationRepository.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    // 읽은 알람 조회
    public List<NotificationResponse> getReadAlarms(Long receiverId) {
        return notificationRepository.findByReceiverIdAndIsReadTrueOrderByCreatedAtDesc(receiverId)
                .stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    // 안 읽은 메시지 여부
    public boolean existUnreadAlarm(Long receiverId) {
        return notificationRepository.existsByReceiverIdAndIsReadFalse(receiverId);
    }
}
