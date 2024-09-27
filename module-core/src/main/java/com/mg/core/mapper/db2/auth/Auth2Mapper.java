package com.mg.core.mapper.db2.auth;

import com.mg.core.dto.mg.UserDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * AuthMapper is a MyBatis mapper interface for authentication-related database
 * operations.
 * This interface provides methods to find user details by login ID and user ID.
 */
@Mapper
public interface Auth2Mapper {

    UserDTO findByLoginId(String loginId);

    UserDTO findByUid(String uid);

}