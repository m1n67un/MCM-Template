package com.mg.api.auth.controller;

import com.mg.api.auth.service.ApiAuthService;
import com.mg.core.common.annotation.MGRestController;
import com.mg.core.dto.mg.LoginRequestDTO;
import com.mg.core.dto.mg.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@MGRestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "AUTH", description = "사용자 인증에 관련된 요청을 처리한다.")
@RequestMapping("/api")
public class ApiAuthController {

    private final ApiAuthService apiAuthService;

    @Operation(summary = "로그인", description = "로그인을 수행하고, 인증에 필요한 토큰을 발급받는다.")
    @PostMapping("/auth/login")
    public TokenDTO login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return apiAuthService.login(loginRequestDTO);
    }

}
