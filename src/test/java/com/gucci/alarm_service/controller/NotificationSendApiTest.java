package com.gucci.alarm_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class NotificationSendApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("성공적으로 알림을 저장하고 응답을 반환한다.")
    @Test
    void 알림_전송_저장_성공() throws Exception {
        // given
        NotificationRequest request = NotificationRequest.builder()
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("테스트 댓글 알림")
                .targetUrl("/posts/1")
                .referenceId(123L)
                .build();

        // when & then
        mockMvc.perform(post("/api/alarm-service/notifications/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("테스트 댓글 알림"))
                .andExpect(jsonPath("$.data.receiverId").value(1L));

        // DB 저장까지 확인
        List<Notification> notifications = notificationRepository.findByReceiverId(1L);
        Assertions.assertThat(notifications).isNotEmpty();
        Assertions.assertThat(notifications.get(0).getContent()).isEqualTo("테스트 댓글 알림");


    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
