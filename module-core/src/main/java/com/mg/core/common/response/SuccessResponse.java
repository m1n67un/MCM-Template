package com.mg.core.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SuccessResponse {

    /**
     * 성공 응답 필드
     */
    private int status;
    private Object data;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * 성공 응답을 생성하는 static 메서드
     *
     * @param status HTTP 상태 코드
     * @param data   응답 데이터
     * @return 성공 응답 객체
     */
    public static SuccessResponse of(int status, Object data) {
        return SuccessResponse.builder()
                .status(status)
                .data(data)
                .build();
    }

}
