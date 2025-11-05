package com.pimp.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 사용자
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_NOT_FOUND_OR_DELETED(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없거나 삭제된 계정입니다."),
    UNAUTHROIZED(HttpStatus.UNAUTHORIZED, "인증되지 않는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
