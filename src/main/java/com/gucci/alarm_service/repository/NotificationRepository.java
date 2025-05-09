package com.gucci.alarm_service.repository;

import com.gucci.alarm_service.domain.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndIsReadFalseOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndIsReadTrueOrderByCreatedAtDesc(Long receiverId);

    List<Notification> findByReceiverIdAndIsReadFalse(Long receiverId);

    void deleteAllByReceiverId(Long receiverId);

    int deleteByIsReadTrueAndCreatedAtBefore(LocalDateTime createdAtBefore);

    int deleteByIsReadFalseAndCreatedAtBefore(LocalDateTime createdAtBefore);

    boolean existsByReceiverIdAndIsReadFalse(Long receiverId);

    Page<Notification> findByReceiverId(Long receiverId, Pageable pageable);

    Page<Notification> findByReceiverIdAndIsReadFalse(Long receiverId, Pageable pageable);

    Page<Notification> findByReceiverIdAndIsReadTrue(Long receiverId, Pageable pageable);

    void deleteByReceiverIdAndId(Long receiverId, Long id);
}