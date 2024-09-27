package com.mg.api.mg.security;

import com.mg.core.common.code.ErrorCode;
import com.mg.core.common.exception.MGException;
import com.mg.core.dto.mg.UserDTO;
import com.mg.core.mapper.db1.auth.AuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailService is a service class that implements Spring Security's
 * UserDetailsService interface
 * to provide custom user authentication logic using MyBatis AuthMapper.
 * This service provides methods to load user details by login ID and user ID.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AuthMapper authMapper;

    /**
     * Loads the user details by login ID.
     *
     * @param loginId the login ID of the user
     * @return the UserDetails object containing user information
     * @throws SPException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String loginId) {
        UserDTO user = authMapper.findByLoginId(loginId);
        if (user == null) {
            throw new MGException(ErrorCode.USER_NON_EXISTS);
        }
        return user;
    }

    /**
     * Loads the user details by user ID.
     *
     * @param uid the unique ID of the user
     * @return the UserDetails object containing user information
     * @throws SPException if the user is not found
     */
    public UserDetails loadUserByUid(String uid) {
        UserDTO user = authMapper.findByUid(uid);
        if (user == null) {
            throw new MGException(ErrorCode.USER_NON_EXISTS);
        }
        return user;
    }

}
