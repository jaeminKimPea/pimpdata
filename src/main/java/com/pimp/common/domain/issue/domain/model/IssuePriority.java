package com.pimp.common.domain.issue.domain.model;

public enum IssuePriority {
    CRITICAL("긴급", "#F44336"),
    HIGH("높음", "#FF9800"),
    NORMAL("보통", "#2196F3"),
    LOW("낮음", "#4CAF50");
    public final String displayName;
    public final String colorHex;
    IssuePriority(String displayName, String colorHex) {
        this.displayName = displayName; this.colorHex = colorHex;
    }
}
