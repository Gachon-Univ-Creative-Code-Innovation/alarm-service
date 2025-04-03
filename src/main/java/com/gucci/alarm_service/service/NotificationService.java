package com.gucci.alarm_service.service;

import com.gucci.alarm_service.common.SseEmitterManager;
import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterManager sseEmitterManager;

    // 알림 저장 및 전송
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

        NotificationResponse saved = NotificationResponse.from(save);

        // 실시간 전송
        sseEmitterManager.send(save.getReceiverId(), saved);

        return saved;
    }

    // SSE 연결
    public SseEmitter subscribe(Long userId) {
        return sseEmitterManager.connect(userId);
    }

    // 전체 알림 조회
    public List<NotificationResponse> getAllAlarams(Long receiverId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(receiverId);

        return notifications.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }

    // 안 읽은 알림 조회
    public List<NotificationResponse> getUnreadAlarms(Long receiverId) {
        List<Notification> unreadAlarams = notificationRepository.findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(receiverId);

        return unreadAlarams.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
    }
}
