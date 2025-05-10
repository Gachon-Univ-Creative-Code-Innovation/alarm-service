package com.gucci.alarm_service.service;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationReadService {
    private final NotificationRepository notificationRepository;

    // 알람 전체 조회
    public Page<NotificationResponse> getAllAlarms(Long receiverId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.findByReceiverId(receiverId, pageable)
                .map(NotificationResponse::from);
    }

    // 안 읽은 알람 조회
    public Page<NotificationResponse> getUnreadAlarms(Long receiverId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.findByReceiverIdAndIsReadFalse(receiverId, pageable)
                .map(NotificationResponse::from);
    }

    // 읽은 알람 조회
    public Page<NotificationResponse> getReadAlarms(Long receiverId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.findByReceiverIdAndIsReadTrue(receiverId, pageable)
                .map(NotificationResponse::from);
    }

    // 안 읽은 메시지 여부
    public boolean existUnreadAlarm(Long receiverId) {
        return notificationRepository.existsByReceiverIdAndIsReadFalse(receiverId);
    }
}
