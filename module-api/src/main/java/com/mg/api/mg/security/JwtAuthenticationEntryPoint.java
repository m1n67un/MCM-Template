package com.mg.api.mg.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mg.core.common.code.ErrorCode;
import com.mg.core.common.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.error("JWT Authentication error: {}", authException.getMessage(), authException);

        ErrorCode errorCode = (ErrorCode) request.getAttribute("errorCode");
        if (errorCode == null) {
            errorCode = ErrorCode.UNAUTHORIZED;
        }

        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
