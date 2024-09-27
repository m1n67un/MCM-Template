package com.mg.core.mapper.db1.userAccess;

import com.mg.core.dto.mg.UserAccessLogDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAccessMapper {
    void insertUserAccessLog(UserAccessLogDTO dto);
}
