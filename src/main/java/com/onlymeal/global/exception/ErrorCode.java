package com.onlymeal.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다"),

    // Auth
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다"),
    // Meal
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "식단 기록을 찾을 수 없습니다"),
    DUPLICATE_MEAL(HttpStatus.CONFLICT, "해당 날짜에 이미 등록된 식사가 있습니다"),

    // Feed
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다"),

    // Food
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, "음식 정보를 찾을 수 없습니다"),
    KEYWORD_REQUIRED(HttpStatus.BAD_REQUEST, "검색어는 필수입니다"),

    // RDI
    RDI_NOT_FOUND(HttpStatus.NOT_FOUND, "권장 섭취량 기준을 찾을 수 없습니다"),

    // File
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "파일이 비어있습니다"),
    FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 파일 형식입니다"),

    // Common
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다");

    private final HttpStatus status;
    private final String message;
}
