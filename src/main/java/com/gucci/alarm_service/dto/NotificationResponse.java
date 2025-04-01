package com.gucci.alarm_service.dto;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private Long receiverId;
    private Long senderId;
    private NotificationType type;
    private String content;
    private String targetUrl;
    private boolean isRead;
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .receiverId(notification.getReceiverId())
                .senderId(notification.getSenderId())
                .type(notification.getType())
                .content(notification.getContent())
                .targetUrl(notification.getTargetUrl())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

}

