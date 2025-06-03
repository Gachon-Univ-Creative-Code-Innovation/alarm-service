package com.gucci.alarm_service.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewPostCreatedEvent {
    private Long postId;
    private Long authorId;
    private String authorNickname;
}
