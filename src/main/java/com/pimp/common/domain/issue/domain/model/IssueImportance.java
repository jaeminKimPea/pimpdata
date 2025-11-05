package com.pimp.common.domain.issue.domain.model;

public enum IssueImportance {
    CRITICAL("심각", "#F44336"),
    HIGH("높음", "#FF9800"),
    NORMAL("보통", "#4CAF50"),
    LOW("낮음", "#9E9E9E");
    public final String displayName;
    public final String colorHex;
    IssueImportance(String displayName, String colorHex) {
        this.displayName = displayName; this.colorHex = colorHex;
    }
}
