package com.mg.core.common.interceptor;

import com.mg.core.dto.mg.UserAccessLogDTO;
import com.mg.core.mapper.db1.userAccess.UserAccessMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.compilermsgs.qual.CompilerMessageKey;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import eu.bitwalker.useragentutils.UserAgent;

@Component
@Slf4j
public class UserAccessLogInterceptor implements HandlerInterceptor {

    private final UserAccessMapper userAccessMapper;

    public UserAccessLogInterceptor(UserAccessMapper userAccessMapper) {
        this.userAccessMapper = userAccessMapper;
    }

    // api 호출 시 먼저 실행됨
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, @NotNull Object handler) {

        // local 에서 실행 시 루프백 주소 출력
        // IPv4, IPv6 지원하는 브라우저마다 출력값이 다름
        // Firefox 는 IPv4 지원, 나머지 대부분 IPv6 지원
        String ipAddress = request.getRemoteAddr();

        String refererUrl = request.getHeader("Referer");
        String requestUrl = request.getRequestURL().toString();
        String userAgent = request.getHeader("User-Agent");

        // userAgent 헤더 확인용
        log.info("USER AGENT : {}", userAgent);

        UserAgent userAgentInfo = UserAgent.parseUserAgentString(userAgent);
        String os = userAgentInfo.getOperatingSystem().getName();

        /*
         * 2024-07-29 일자 각 browser 별 user Agent 문자열
         * 
         * - 크롬
         * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
         * Gecko) Chrome/127.0.0.0 Safari/537.36
         * 
         * - 엣지
         * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
         * Gecko) Chrome/127.0.0.0 Safari/537.36 Edg/127.0.0.0
         * 
         * - 웨일
         * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
         * Gecko) Chrome/126.0.0.0 Whale/3.27.254.15 Safari/537.36
         * 
         * - 파이어폭스
         * Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:128.0) Gecko/20100101
         * Firefox/128.0
         * 
         * - 오페라
         * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like
         * Gecko) Chrome/126.0.0.0 Safari/537.36 OPR/112.0.0.0
         * 
         * - 사파리
         * Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML,
         * like Gecko) Version/17.5 Safari/605.1.15
         * 
         * - chrome, safari 는 AppleWebKit 검색 엔진 사용
         * - Firefox는 Gecko 검색 엔진 사용
         * - edge, whale 은 chrome 계열 브라우저
         * - 웬만한 브라우저는 chrome, safari 정보를 포함 => 웹페이지 랜더링시 보다 나은 호환성 보장
         * - 따라서 chrome, safari는 else if 판별 우선순위가 떨어짐(edge, whale, opera 먼저 판별)
         */

        String browser;
        String browserVersion;
        if (StringUtils.containsIgnoreCase(userAgent, "Edg")) {// 엣지
            browser = "Edge";
            browserVersion = versionExtractor(userAgent, "Edg");
        } else if (StringUtils.containsIgnoreCase(userAgent, "Whale")) {// 웨일
            browser = "Whale";
            browserVersion = versionExtractor(userAgent, "Whale");
        } else if (StringUtils.containsIgnoreCase(userAgent, "OPR")) {// 오페라
            browser = "Opera";
            browserVersion = versionExtractor(userAgent, "OPR");
        } else if (StringUtils.containsIgnoreCase(userAgent, "Chrome")) {// 크롬
            browser = "Chrome";
            browserVersion = versionExtractor(userAgent, "Chrome");
        } else if (StringUtils.containsIgnoreCase(userAgent, "Firefox")) {// 파이어폭스
            browser = "Firefox";
            browserVersion = versionExtractor(userAgent, "Firefox");
        } else if (StringUtils.containsIgnoreCase(userAgent, "Safari")) {// 사파리
            browser = "Safari";
            // 사파리에서는 독자적으로 버전 항목이 존재
            browserVersion = userAgentInfo.getBrowserVersion().getVersion();
        } else {
            browser = "Unknown";
            browserVersion = "Unknown";
        }

        String deviceType = userAgentInfo.getOperatingSystem().getDeviceType().getName();

        UserAccessLogDTO userAccessLogDTO = UserAccessLogDTO.builder()
                .ipAddress(ipAddress)
                .requestUrl(requestUrl)
                .referrerUrl(refererUrl)
                .userAgent(userAgent)
                .browser(browser)
                .browserVersion(browserVersion)
                .operatingSystem(os)
                .deviceType(deviceType)
                .build();

        // db 로그 적재
        userAccessMapper.insertUserAccessLog(userAccessLogDTO);

        return true;
    }

    // user agent 에서 브라우저 별 버전 추출
    public String versionExtractor(String input, String prefix) {
        int prefixIndex = input.indexOf(prefix);
        int startIndex = prefixIndex + prefix.length() + 1; // +1 for the slash
        int endIndex = input.indexOf(" ", startIndex);
        if (endIndex == -1) {
            endIndex = input.length();
        }

        return input.substring(startIndex, endIndex);
    }
}
