package com.gucci.alarm_service.service;

import com.gucci.alarm_service.domain.Notification;
import com.gucci.alarm_service.domain.NotificationType;
import com.gucci.alarm_service.dto.NotificationRequest;
import com.gucci.alarm_service.dto.NotificationResponse;
import com.gucci.alarm_service.repository.NotificationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationWriteService notificationWriteService;

    @InjectMocks
    private NotificationReadService notificationReadService;

    @Test
    void 알림을_정상적으로_저장할_수_있다() {
        //given
        NotificationRequest request = NotificationRequest.builder()
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("댓글이 달렸습니다.")
                .targetUrl("/blog/123")
                .referenceId(123L)
                .build();

        Notification expected = Notification.builder()
                .receiverId(request.getReceiverId())
                .senderId(request.getSenderId())
                .type(request.getType())
                .content(request.getContent())
                .targetUrl(request.getTargetUrl())
                .referenceId(request.getReferenceId())
                .build();


        when(notificationRepository.save(Mockito.any(Notification.class))).thenReturn(expected);

        //when
        NotificationResponse saved = notificationWriteService.save(request);

        //then
        assertThat(saved.getReceiverId()).isEqualTo(expected.getReceiverId());
        assertThat(saved.getType()).isEqualTo(expected.getType());
        assertThat(saved.getContent()).isEqualTo(expected.getContent());
    }

    @DisplayName("안 읽은 알림만 필터링해서 반환")
    @Test
    void 안_읽은_알림_반환_성공() {
        // given
        Long receiverId = 1L;
        int page = 0;
        int size = 20;

        Notification read = Notification.builder()
                .receiverId(receiverId)
                .senderId(2L)
                .type(NotificationType.FOLLOW)
                .content("읽은 알림")
                .targetUrl("/url")
                .isRead(true)
                .build();

        Notification unread = Notification.builder()
                .receiverId(receiverId)
                .senderId(3L)
                .type(NotificationType.FOLLOW)
                .content("안 읽은 알림")
                .targetUrl("/url")
                .isRead(false)
                .build();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<Notification> notifications = List.of(unread);
        Page<Notification> notificationPage = new PageImpl<>(notifications, pageable, notifications.size());

        given(notificationRepository.findByReceiverIdAndIsReadFalse(receiverId, pageable))
                .willReturn(notificationPage);

        // when
        Page<NotificationResponse> result = notificationReadService.getUnreadAlarms(receiverId, page, size);

        //then
        assertThat(result).hasSize(1);
//        assertThat(result.get(0).isRead()).isFalse();
//        assertThat(result.get(0).getSenderId()).isEqualTo(3L);

    }

    @DisplayName("알림을 읽음 처리 할 수 있다.")
    @Test
    void 알림_읽음_처리_성공() {
        // given
        Long notificationId = 1L;
        Notification notification = Notification.builder()
                .id(notificationId)
                .receiverId(1L)
                .senderId(2L)
                .type(NotificationType.COMMENT)
                .content("댓글이 달렸습니다.")
                .targetUrl("/posts/1")
                .isRead(false)
                .build();

        given(notificationRepository.findById(notificationId)).willReturn(Optional.of(notification));

        // when
        notificationWriteService.markRead(notificationId);

        // then
        assertThat(notification.isRead()).isTrue();
        then(notificationRepository).should().findById(notificationId);

    }

    @Test
    @DisplayName("읽지 않은 알림이 존재할 때 true를 반환한다")
    void 읽지_않은_알림_존재_true_반환(){
        // given
        Long userId = 1L;
        when(notificationRepository.existsByReceiverIdAndIsReadFalse(userId)).thenReturn(true);

        //when
        boolean result = notificationReadService.existUnreadAlarm(userId);

        //then
        assertThat(result).isTrue();
        verify(notificationRepository).existsByReceiverIdAndIsReadFalse(userId);
    }

    @Test
    @DisplayName("읽지 않은 알림이 존재하지 않을 때 false를 반환한다")
    void 읽지_않은_알림_존재하지_않으면_false_반환(){
        // given
        Long userId = 1L;
        when(notificationRepository.existsByReceiverIdAndIsReadFalse(userId)).thenReturn(false);

        //when
        boolean result = notificationReadService.existUnreadAlarm(userId);

        //then
        assertThat(result).isFalse();
        verify(notificationRepository).existsByReceiverIdAndIsReadFalse(userId);
    }
}
