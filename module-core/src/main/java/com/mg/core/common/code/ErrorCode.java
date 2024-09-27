package com.mg.core.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Common Error Code
     */
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "IPR", "유효하지 않은 파라미터 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ISR", "서버 오류가 발생했습니다."),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "MNSD", "지원되지 않는 HTTP 메소드입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NFD", "리소스를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ADD", "접근 가능한 권한이 없습니다."),
    USER_NON_EXISTS(HttpStatus.UNAUTHORIZED, "UNFD", "존재하지 않는 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "PNMH", "비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UAD", "인증되지 않은 접근입니다."),

    /**
     * Token Error Code
     */
    TOKEN_NON_EXISTS(HttpStatus.UNAUTHORIZED, "TNES", "토큰이 존재하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "ITN", "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TED", "만료된 토큰입니다."),

    /**
     * Custom Error Code
     */
    SAMPLE(HttpStatus.BAD_REQUEST, "SAMPLE_001", "에러코드 샘플");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
