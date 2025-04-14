package com.gucci.alarm_service.controller;


import com.gucci.alarm_service.auth.JwtUserAuthentication;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.service.AuthServiceHelper;
import com.gucci.alarm_service.service.NotificationEventHandler;
import com.gucci.alarm_service.service.NotificationReadService;
import com.gucci.alarm_service.service.NotificationWriteService;
import com.gucci.common.exception.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@DisplayName("알림 컨트롤러 테스트")
@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationEventHandler notificationEventHandler;

    @MockBean
    private NotificationWriteService notificationWriteService;

    @MockBean
    private NotificationReadService notificationReadService;

    @MockBean
    private AuthServiceHelper authServiceHelper;

    private static final Long MOCK_USER_ID = 1L;

//    @BeforeEach
//    void setUpSecurityContext() {
//        JwtUserAuthentication authentication = new JwtUserAuthentication(MOCK_USER_ID);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    @AfterEach
//    void clearSecurityContext() {
//        SecurityContextHolder.clearContext();
//    }


    @DisplayName("알림을 읽음 처리 할 수 있다.")
    @Test
    void 읽음_처리_성공() throws Exception {
        // given
        Long notificationId = 1L;

        // when & then
        mockMvc.perform(patch("/api/alarm-service/notifications/{id}/read", notificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("요청이 성공적으로 처리되었습니다."))
                .andDo(print());

    }

    @DisplayName("전체 알림을 조회할 수 있다.")
    @Test
    void 전체_알림_조회_성공() throws Exception {
        // given
        Long receiverId = 1L;

        NotificationResponse notice1 = NotificationResponse.builder()
                .id(1L)
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("새 댓글이 달렸어요")
                .targetUrl("/posts/1")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        NotificationResponse notice2 = NotificationResponse.builder()
                .id(2L)
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.FOLLOW)
                .content("누군가 당신을 팔로우했어요")
                .targetUrl("/follow/2")
                .isRead(true)
                .createdAt(LocalDateTime.now())
                .build();



    }


    @DisplayName("안 읽은 알림을 조회할 수 있다.")
    @Test
    void 안읽은_알림_조회_성공() throws Exception {
        // given
        Long receiverId = 1L;

        NotificationResponse unreadNotice = NotificationResponse.builder()
                .id(1L)
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("새 댓글이 달렸어요")
                .targetUrl("/posts/1")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        List<NotificationResponse> mockUnreadAlarms = List.of(unreadNotice);


        ArgumentCaptor<Authentication> captor = ArgumentCaptor.forClass(Authentication.class);

        given(authServiceHelper.getCurrentUserId(captor.capture())).willReturn(receiverId);
        given(notificationReadService.getUnreadAlarms(receiverId)).willReturn(mockUnreadAlarms);

        // when & then
        mockMvc.perform(get("/api/alarm-service/notifications/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("데이터 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(unreadNotice.getId()))
                .andExpect(jsonPath("$.data[0].read").value(false))
                .andDo(print());

    }

}
