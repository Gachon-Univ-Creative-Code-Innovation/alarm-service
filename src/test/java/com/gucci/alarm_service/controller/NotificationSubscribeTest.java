package com.gucci.alarm_service.controller;

import com.gucci.alarm_service.service.NotificationEventHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootTest
@ActiveProfiles("test")
public class NotificationSubscribeTest {

    @Autowired
    private NotificationEventHandler notificationEventHandler;

    @DisplayName("SSE 구독 요청 시 emitter가 정상적으로 생성된다.")
    @Test
    void SSE_구독_테스트() {
        // given
        Long userId = 1L;

        // when
        SseEmitter emitter = notificationEventHandler.subscribe(userId);

        // then
        Assertions.assertThat(emitter).isNotNull();

    }
}
