package com.gucci.alarm_service.dto;

import com.gucci.alarm_service.domain.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequest {
    private Long receiverId;
    private Long senderId;
    private NotificationType type;
    private String content;
    private String targetUrl;
    private Long referenceId;
}
