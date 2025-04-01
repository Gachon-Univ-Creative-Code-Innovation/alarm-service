package com.gucci.alarm_service.repository;

import com.gucci.alarm_service.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
