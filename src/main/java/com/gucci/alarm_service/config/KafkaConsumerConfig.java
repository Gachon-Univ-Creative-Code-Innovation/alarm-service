package com.gucci.alarm_service.config;


import com.gucci.alarm_service.kafka.dto.NewCommentCreatedEvent;
import com.gucci.alarm_service.kafka.dto.NewPostCreatedEvent;
import com.gucci.alarm_service.kafka.dto.NewReplyCreatedEvent;
import com.gucci.alarm_service.kafka.dto.NotificationKafkaRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, NotificationKafkaRequest> consumerFactory() {
        JsonDeserializer<NotificationKafkaRequest> deserializer = new JsonDeserializer<>(NotificationKafkaRequest.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "alarm-group",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        ), new StringDeserializer(), deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationKafkaRequest> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NotificationKafkaRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, NewPostCreatedEvent> postEventConsumerFactory() {
        JsonDeserializer<NewPostCreatedEvent> deserializer = new JsonDeserializer<>(NewPostCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "alarm-group",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        ), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewPostCreatedEvent> postEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NewPostCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(postEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, NewCommentCreatedEvent> commentEventConsumerFactory() {
        JsonDeserializer<NewCommentCreatedEvent> deserializer = new JsonDeserializer<>(NewCommentCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "alarm-group",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        ), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewCommentCreatedEvent> commentEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NewCommentCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(commentEventConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, NewReplyCreatedEvent> replyEventConsumerFactory() {
        JsonDeserializer<NewReplyCreatedEvent> deserializer = new JsonDeserializer<>(NewReplyCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, "alarm-group",
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        ), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NewReplyCreatedEvent> replyEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, NewReplyCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(replyEventConsumerFactory());
        return factory;
    }
}
