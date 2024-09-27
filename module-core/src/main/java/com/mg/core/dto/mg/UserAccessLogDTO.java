package com.mg.core.dto.mg;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "사용자 접속 로그 DTO")
public class UserAccessLogDTO {

    @Schema(description = "table index")
    private String idx;
    @Schema(description = "아이피 주소")
    private String ipAddress;
    @Schema(description = "리퍼럴 url")
    private String referrerUrl;
    @Schema(description = "요청 url")
    private String requestUrl;
    @Schema(description = "브라우저 User-Agent 헤더 정보")
    private String userAgent;
    @Schema(description = "브라우저 이름")
    private String browser;
    @Schema(description = "브라우저 버전")
    private String browserVersion;
    @Schema(description = "운영체제(OS)")
    private String operatingSystem;
    @Schema(description = "접속한 기기")
    private String deviceType;
    @Schema(description = "접근 시간")
    private Date accessTime;

}
