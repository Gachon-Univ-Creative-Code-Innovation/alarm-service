package com.gucci.alarm_service.scheduler;

import com.gucci.alarm_service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {
    private final NotificationRepository notificationRepository;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime now = LocalDateTime.now();

        // 읽은 알림 -> 7일 경과 시 삭제
        LocalDateTime readLimit = now.minusDays(7);
        int deletedRead = notificationRepository.deleteByIsReadTrueAndCreatedAtBefore(readLimit);

        // 안 읽은 알림 -> 100일 경과 시 삭제
        LocalDateTime unreadLimit = now.minusDays(100);
        int deletedUnread = notificationRepository.deleteByIsReadFalseAndCreatedAtBefore(unreadLimit);

        log.info("알림 정리 완료 - 읽은 알림: {}, 안 읽은 알림: {}", deletedRead, deletedUnread);
    }

}
