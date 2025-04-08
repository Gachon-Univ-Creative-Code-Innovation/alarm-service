package com.gucci.alarm_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationKafkaRequest {
    private Long receiverId;
    private Long senderId;
    private String type;
    private String content;
    private String targetUrl;
}
