package com.mg.api.mg.security;

import com.mg.core.common.code.TokenType;
import com.mg.core.dto.mg.UserDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private final Key key;
    private final int accessTokenExpiration;
    private final int refreshTokenExpiration;
    private final String issuer;

    public JwtTokenProvider(@Value("${jwt.secret.key}") String jwtSecret,
            @Value("${jwt.access.token.expiration}") int accessTokenExpiration,
            @Value("${jwt.refresh.token.expiration}") int refreshTokenExpiration,
            @Value("${jwt.issuer}") String issuer) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.issuer = issuer;
    }

    /**
     * Generate AccessToken
     *
     * @param userDTO User info
     * @return AccessToken (expiration 30 min)
     */
    public String generateAccessToken(UserDTO userDTO) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(accessTokenExpiration, ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(userDTO.getUid())
                .claim("tokenType", TokenType.ACCESS.name())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Generate RefreshToken
     *
     * @param userDTO User info
     * @return RefreshToken (expiration 30 day)
     */
    public String generateRefreshToken(UserDTO userDTO) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(refreshTokenExpiration, ChronoUnit.DAYS);

        return Jwts.builder()
                .setSubject(userDTO.getUid())
                .claim("tokenType", TokenType.REFRESH.name())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Get user uid(PK) from a given JWT token
     *
     * @param token JWT token
     * @return user uid(PK)
     */
    public String getUidFromToken(String token) {
        Claims claims = parseClaims(token);
        log.info("""
                Claims: {}
                Subject(uid, PK): {}
                """, claims, claims.getSubject());
        return claims.getSubject();
    }

    /**
     * Parse the claims from a given JWT token
     *
     * @param token JWT token
     * @return the claims extracted from the JWT token
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Validate JWT token
     *
     * @param token JWT token
     * @return 0 if the token is valid, 1 if the token is expored, 2 if the token is
     *         not valid
     */
    public int validateToken(String token) {
        try {
            parseClaims(token);
            return 0;
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage(), e);
            return 1;
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage(), e);
            return 2;
        }
    }

    /**
     * Check if JWT token is expired
     *
     * @param token JWT token
     * @return true if the token is expired or an error occurs, false otherwise
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.error("JWT expired error: {}", e.getMessage(), e);
            return true;
        } catch (Exception e) {
            log.error("Error occurred while checking if JWT is expired: {}", e.getMessage(), e);
            return true;
        }
    }

    /**
     * Checks if the JWT token is an access token.
     *
     * @param token JWT token
     * @return true if the token is an access token, false otherwise
     */
    public boolean isAccessToken(String token) {
        Claims claims = parseClaims(token);
        String tokenType = claims.get("tokenType", String.class);
        return TokenType.ACCESS.name().equals(tokenType);
    }

}
