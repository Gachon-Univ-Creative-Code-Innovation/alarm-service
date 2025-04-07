package com.gucci.alarm_service.kafka.consumer;

import com.gucci.alarm_service.dto.NotificationKafkaRequest;
import com.gucci.alarm_service.service.NotificationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationConsumer {
    private final NotificationEventHandler notificationEventHandler;

    @KafkaListener(topics = "alarm-topic", groupId = "alarm-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(NotificationKafkaRequest message) {
        log.info("받은 Kafka 메시지 : {}", message);

        notificationEventHandler.handle(message);
    }
}
