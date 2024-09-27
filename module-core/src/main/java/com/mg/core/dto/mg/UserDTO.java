package com.mg.core.dto.mg;

import com.mg.core.common.code.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "유저 DTO")
public class UserDTO extends CommonDTO implements UserDetails {

    @Schema(description = "유저 인덱스")
    private String uid;
    @Schema(description = "유저 아이디")
    private String loginId;
    @Schema(description = "유저 비밀번호")
    private String password;
    @Schema(description = "이름")
    private String userNm;
    @Schema(description = "이메일")
    private String userEmail;
    @Schema(description = "휴대폰 번호")
    private String cellPhone;
    @Schema(description = "주소")
    private String addr;
    @Schema(description = "주소 상세")
    private String addrDtl;
    @Schema(description = "유저 타입(회원: USER, 관리자: ADMIN)")
    private String userType;
    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = Role.fromString(userType);
        return List.of(new SimpleGrantedAuthority(role.getRole()));
    }

    @Override
    public String getUsername() {
        return loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
