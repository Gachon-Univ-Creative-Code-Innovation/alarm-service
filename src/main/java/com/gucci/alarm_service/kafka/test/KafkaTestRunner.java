package com.gucci.alarm_service.kafka.test;

import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.dto.NotificationKafkaRequest;
import com.gucci.alarm_service.kafka.producer.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaTestRunner implements CommandLineRunner {

    private final NotificationProducer notificationProducer;

    @Override
    public void run(String... args) {
        NotificationKafkaRequest message = NotificationKafkaRequest.builder()
                .receiverId(1L)
                .senderId(2L)
                .type(String.valueOf(NotificationType.COMMENT))
                .content("Kafka로부터 온 새 댓글 알림")
                .targetUrl("/posts/1")
                .build();

        notificationProducer.sendNotification(message);
    }
}
