package com.gucci.alarm_service.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long receiverId;

    private Long senderId; // 시스템 알림일 시 null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false)
    private String content;

    private String targetUrl;

    private Long referenceId;

    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();
}
