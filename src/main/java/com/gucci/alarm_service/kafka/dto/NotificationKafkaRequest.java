package com.gucci.alarm_service.kafka.dto;

import lombok.*;

@Getter
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
