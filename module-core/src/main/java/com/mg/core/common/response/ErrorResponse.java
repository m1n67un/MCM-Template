package com.mg.core.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mg.core.common.code.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    /**
     * 에러 응답 필드
     */
    private int status;
    private String errorCode;
    private String message;
    private String errorMessageDev;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 에러 응답을 생성하는 static 메서드
     *
     * @param errorCode 에러코드
     */
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .build();
    }

    /**
     * 에러 응답을 생성하는 static 메서드 (개발 환경용 상세 메세지 포함)
     *
     * @param errorMessageDev 에러 상세 메세지
     * @param errorCode       에러코드
     */
    public static ErrorResponse of(ErrorCode errorCode, String errorMessageDev) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .errorCode(errorCode.getErrorCode())
                .message(errorCode.getMessage())
                .errorMessageDev(errorMessageDev)
                .build();
    }

}
