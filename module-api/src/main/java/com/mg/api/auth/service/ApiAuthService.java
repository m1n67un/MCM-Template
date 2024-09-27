package com.mg.api.auth.service;

import com.mg.api.mg.security.CustomUserDetailService;
import com.mg.api.mg.security.JwtTokenProvider;
import com.mg.core.common.code.ErrorCode;
import com.mg.core.common.exception.MGException;
import com.mg.core.dto.mg.LoginRequestDTO;
import com.mg.core.dto.mg.TokenDTO;
import com.mg.core.dto.mg.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiAuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Login
     * 
     * @param loginRequestDTO 로그인 요청정보
     * @return accessToken & refreshToken
     */
    public TokenDTO login(LoginRequestDTO loginRequestDTO) {
        UserDTO userDetail = (UserDTO) customUserDetailService.loadUserByUsername(loginRequestDTO.getLoginId());

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), userDetail.getPassword())) {
            throw new MGException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        return TokenDTO.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(userDetail))
                .refreshToken(jwtTokenProvider.generateRefreshToken(userDetail))
                .build();
    }

}
