package com.gucci.alarm_service;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.repository.NotificationRepository;
import com.gucci.alarm_service.scheduler.NotificationCleanupScheduler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
public class SchedulerIntegrationTest {

    @Autowired
    private NotificationCleanupScheduler scheduler;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
    }

    @DisplayName("스케줄러가 오래된 알림을 삭제한다")
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void 스케줄러_알림_삭제_테스트() {
        // given
        Notification oldRead = Notification.builder()
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("오래된 읽은 알림")
                .targetUrl("/posts/1")
                .isRead(true)
                .createdAt(LocalDateTime.now().minusDays(8))
                .build();

        Notification oldUnread = Notification.builder()
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.FOLLOW)
                .content("오래된 안 읽은 알림")
                .targetUrl("/users/2")
                .isRead(false)
                .createdAt(LocalDateTime.now().minusDays(101))
                .build();

        notificationRepository.save(oldRead);
        notificationRepository.save(oldUnread);

        // when
        scheduler.deleteOldNotifications();
        // then
        Assertions.assertThat(notificationRepository.findAll()).isEmpty();
    }
}
