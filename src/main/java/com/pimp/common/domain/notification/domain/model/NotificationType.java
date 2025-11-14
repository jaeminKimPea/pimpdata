package com.pimp.common.domain.notification.domain.model;

import lombok.Getter;

@Getter
public enum NotificationType {
    MATCHING("매칭"),
    MESSAGE("채팅"),
    CALL("통화");

    private final String description;

    NotificationType(String description) {
        this.description = description;
    }

}