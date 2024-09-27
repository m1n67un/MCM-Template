package com.mg.api.mg.security;

import com.mg.core.common.code.ErrorCode;
import com.mg.core.dto.mg.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;
import java.util.Optional;

/**
 * filter that intercepts HTTP requests to validate JWT tokens.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {
                "/email/**"
        };
        String path = request.getRequestURI();
        log.info("Request URI - {}", path);
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        log.info("boolean flag - {}",
                Arrays.stream(excludePath).anyMatch(pattern -> antPathMatcher.match(pattern, path)));
        return Arrays.stream(excludePath).anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            getJwtFromRequest(request).ifPresentOrElse(
                    token -> {
                        switch (jwtTokenProvider.validateToken(token)) {
                            case 0 -> handleValidToken(token, request);
                            case 1 -> request.setAttribute("errorCode", ErrorCode.TOKEN_EXPIRED);
                            case 2 -> request.setAttribute("errorCode", ErrorCode.INVALID_TOKEN);
                        }
                    },
                    () -> request.setAttribute("errorCode", ErrorCode.TOKEN_NON_EXISTS));

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error occured", e);
            request.setAttribute("errorCode", ErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * Extracts the JWT token from the request's Authorization header.
     *
     * @param request the HTTP request
     * @return an Optional containing the JWT token if present, otherwise an empty
     *         Optional
     */
    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(bearerToken -> StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer "))
                .map(bearerToken -> bearerToken.substring(7));
    }

    /**
     * Handles the validation and authentication setup for a valid token.
     *
     * @param token   the valid JWT token
     * @param request the HTTP request
     */
    private void handleValidToken(String token, HttpServletRequest request) {
        if (jwtTokenProvider.isAccessToken(token)) {
            String uid = jwtTokenProvider.getUidFromToken(token);
            UserDTO userDetail = (UserDTO) customUserDetailService.loadUserByUid(uid);
            setAuthentication(request, userDetail);
        } else {
            request.setAttribute("errorCode", ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * Sets the authentication in the SecurityContext with the given user details.
     *
     * @param request the HTTP request
     * @param userDTO the user details to authenticate
     */
    private void setAuthentication(HttpServletRequest request, UserDTO userDTO) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDTO, null,
                userDTO.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
