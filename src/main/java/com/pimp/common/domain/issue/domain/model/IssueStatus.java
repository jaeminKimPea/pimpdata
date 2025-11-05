package com.pimp.common.domain.issue.domain.model;

public enum IssueStatus {
    REGISTERED("등록", "#2196F3"),
    IN_PROGRESS("진행", "#FF9800"),
    RESOLVED("해결", "#4CAF50"),
    CLOSED("완료", "#9E9E9E");
    public final String displayName;
    public final String colorHex;
    IssueStatus(String displayName, String colorHex) {
        this.displayName = displayName; this.colorHex = colorHex;
    }
}
