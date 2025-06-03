package com.gucci.alarm_service.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.kafka.dto.NewCommentCreatedEvent;
import com.gucci.alarm_service.kafka.dto.NewReplyCreatedEvent;
import com.gucci.alarm_service.kafka.dto.NotificationKafkaRequest;
import com.gucci.alarm_service.kafka.dto.NewPostCreatedEvent;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "alarm-topic", groupId = "alarm-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(NotificationKafkaRequest message) {
        try {
            log.info("받은 Kafka 메시지 : {}", message);
            notificationEventHandler.handle(message);
        } catch (Exception e) {
            log.error("alarm-topic 역직렬화 실패", e);
        }
    }

    @KafkaListener(topics = "post.created", groupId = "alarm-group", containerFactory = "postEventKafkaListenerContainerFactory")
    public void postConsume(NewPostCreatedEvent message) {
        try {
            log.info("받은 Kafka 메시지 : {}", message);

            NotificationKafkaRequest alarmMessage = NotificationKafkaRequest.builder()
                    .receiverId(1L) // TODO: 실제 팔로워 ID 리스트 처리 필요
                    .senderId(message.getAuthorId())
                    .type(NotificationType.POST.toString())
                    .content(message.getAuthorNickname() + "님이 새 글을 작성했습니다.")
                    .targetUrl(message.getPostId().toString())
                    .build();

            notificationEventHandler.handle(alarmMessage);
        } catch (Exception e) {
            log.error("post.created 역직렬화 실패", e);
        }
    }

    @KafkaListener(topics = "comment.created", groupId = "alarm-group", containerFactory = "commentEventKafkaListenerContainerFactory")
    public void commentConsume(NewCommentCreatedEvent message) {
        try {
            log.info("받은 Kafka 메시지 : {}", message);

            NotificationKafkaRequest alarmMessage = NotificationKafkaRequest.builder()
                    .receiverId(message.getAuthorId())
                    .senderId(message.getCommenterId())
                    .type(NotificationType.COMMENT.toString())
                    .content(message.getCommenterNickname() + "님이 댓글을 작성했습니다.")
                    .targetUrl(message.getPostId().toString())
                    .build();

            notificationEventHandler.handle(alarmMessage);
        } catch (Exception e) {
            log.error("comment.created 역직렬화 실패", e);
        }
    }

    @KafkaListener(topics = "reply.created", groupId = "alarm-group",containerFactory = "replyEventKafkaListenerContainerFactory")
    public void replyConsume(NewReplyCreatedEvent message) {
        try {
            log.info("받은 Kafka 메시지 : {}", message);

            NotificationKafkaRequest alarmMessage = NotificationKafkaRequest.builder()
                    .receiverId(message.getReceiverId())
                    .senderId(message.getCommenterId())
                    .type(NotificationType.REPLY.toString())
                    .content(message.getCommenterNickname() + "님이 답글을 남겼습니다.")
                    .targetUrl(message.getPostId().toString())
                    .build();

            notificationEventHandler.handle(alarmMessage);
        } catch (Exception e) {
            log.error("reply.created 역직렬화 실패", e);
        }
    }
}