package com.gucci.alarm_service.domain;

public enum NotificationType {
    FOLLOW,     // 누군가 나를 팔로우 했을 때
    COMMENT,    // 내가 쓴 글에 댓글이 달렸을 때
    POST,       // 팔로잉 한 사람이 새 글을 썼을 때
    REPLY,      // 답글이 왔을 때
    SYSTEM      // 시스템 공지, 이벤트 안내 등
}
