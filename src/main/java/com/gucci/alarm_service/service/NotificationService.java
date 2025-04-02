package com.gucci.alarm_service.service;

import com.gucci.alarm_service.common.SseEmitterManager;
import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterManager sseEmitterManager;

    public NotificationResponse save(NotificationRequest request){
        Notification notification = Notification.builder()
                .receiverId(request.getReceiverId())
                .senderId(request.getSenderId())
                .type(request.getType())
                .content(request.getContent())
                .targetUrl(request.getTargetUrl())
                .referenceId(request.getReferenceId())
                .build();

        Notification save = notificationRepository.save(notification);

        return NotificationResponse.from(save);
    }

    public SseEmitter subscribe(Long userId) {
        return sseEmitterManager.connect(userId);
    }
}
