package com.gucci.alarm_service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationSseEventDTO {
    private NotificationResponse notification;
    private boolean isExistAlarm;


    public static NotificationSseEventDTO from(NotificationResponse notification, boolean isExistAlarm) {
        return NotificationSseEventDTO.builder()
                .notification(notification)
                .isExistAlarm(isExistAlarm)
                .build();
    }
}
