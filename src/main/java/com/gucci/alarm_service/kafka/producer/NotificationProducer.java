package com.gucci.alarm_service.kafka.producer;

import com.gucci.alarm_service.dto.NotificationKafkaRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationKafkaRequest> kafkaTemplate;

    public void sendNotification(NotificationKafkaRequest message) {
        log.info("Kafka 알림 전송: {}", message);
        kafkaTemplate.send("alarm-topic", message);
    }
}
