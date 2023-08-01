package com.example.wanted.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode {
    // User
    // 400 BAD_REQUEST - 잘못된 요청
//    NOT_VALID_EMAIL(BAD_REQUEST, "유효하지 않은 이메일 입니다."),
//    DUPLICATE_NICK_NAME(BAD_REQUEST, "닉네임이 동일합니다."),
    // 401 Unauthorized - 권한 없음
    INVALID_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    // 404 Not Found - 찾을 수 없음
    PASSWORD_MISMATCH(NOT_FOUND, "비밀번호가 일치하지 않습니다."),
    EMAIL_NOT_FOUND(NOT_FOUND, "이메일이 존재하지 않습니다."),
//    USER_NOT_FOUND(NOT_FOUND, "등록된 사용자가 없습니다"),
    // 409
    DUPLICATE_EMAIL(CONFLICT, "중복된 이메일이 존재합니다."),

    // Post
    // 404 Not Found - 찾을 수 없음
    POST_NOT_FOUND(NOT_FOUND, "게시글이 존재하지 않습니다."),


   ;


    private final HttpStatus httpStatus;
    private final String message;
}
