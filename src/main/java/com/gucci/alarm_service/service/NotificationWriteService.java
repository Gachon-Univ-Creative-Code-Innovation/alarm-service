package com.gucci.alarm_service.service;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import com.gucci.common.exception.CustomException;
import com.gucci.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationWriteService {
    private final NotificationRepository notificationRepository;

    // 알림 저장
    public NotificationResponse save(NotificationRequest request) {
        Notification notification = notificationRepository.save(Notification.builder()
                .receiverId(request.getReceiverId())
                .senderId(request.getSenderId())
                .type(request.getType())
                .content(request.getContent())
                .targetUrl(request.getTargetUrl())
                .referenceId(request.getReferenceId())
                .build());

        return NotificationResponse.from(notification);
    }

    // 읽음 처리
    @Transactional
    public void markRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_ARGUMENT));

        notification.markAsRead();
    }


    // 전체 읽음 처리
    @Transactional
    public void markReadAll(Long receiverId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdAndIsReadFalse(receiverId);

        notifications.forEach(Notification::markAsRead);
    }
}
