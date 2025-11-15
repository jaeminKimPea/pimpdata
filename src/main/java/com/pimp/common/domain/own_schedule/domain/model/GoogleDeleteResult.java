package com.pimp.common.domain.own_schedule.domain.model;

public enum GoogleDeleteResult {
    SUCCESS,              // 정상 삭제됨
    ALREADY_DELETED,      // 410 Gone
    NOT_FOUND,            // 404 Not found
    ERROR,                 // 예상 못한 에러
    SKIPPED
}
