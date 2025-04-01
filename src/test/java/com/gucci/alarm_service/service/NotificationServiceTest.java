package com.gucci.alarm_service.service;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void 알림을_정상적으로_저장할_수_있다() {
        //given
        Notification notification = Notification.builder()
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("댓글이 달렸습니다.")
                .targetUrl("/blog/123")
                .referenceId(123L)
                .build();

        Mockito.when(notificationRepository.save(notification)).thenReturn(notification);

        //when
        Notification saved = notificationService.save(notification);

        //then
        assertThat(saved.getReceiverId()).isEqualTo(1L);
        assertThat(saved.getType()).isEqualTo(NotificationType.COMMENT);
        assertThat(saved.getContent()).isEqualTo("댓글이 달렸습니다.");
    }
}
