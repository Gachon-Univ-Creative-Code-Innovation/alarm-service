package com.gucci.alarm_service.service;

import com.gucci.alarm_service.common.SseEmitterManager;
import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.dto.NotificationKafkaRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.dto.NotificationSseEventDTO;
import com.gucci.alarm_service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationEventHandler {
    private final SseEmitterManager sseEmitterManager;
    private final NotificationRepository notificationRepository;

    // 알림 생성, 실시간 전송
    @Transactional
    public void handle(NotificationKafkaRequest message) {
        Notification notification = Notification.builder()
                .receiverId(message.getReceiverId())
                .senderId(message.getSenderId())
                .type(NotificationType.valueOf(message.getType()))
                .content(message.getContent())
                .targetUrl(message.getTargetUrl())
                .build();

        Notification saved = notificationRepository.save(notification);
        NotificationResponse notificationResponse = NotificationResponse.from(saved);

        boolean hasUnread = notificationRepository.existsByReceiverIdAndIsReadFalse(message.getReceiverId());

        NotificationSseEventDTO responseSSE = NotificationSseEventDTO.from(notificationResponse, hasUnread);

        sseEmitterManager.send(notification.getReceiverId(), responseSSE);
    }

    public SseEmitter subscribe(Long userId) {
        return sseEmitterManager.connect(userId);
    }
}
