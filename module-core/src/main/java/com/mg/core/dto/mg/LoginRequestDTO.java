package com.mg.core.dto.mg;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDTO {

    @NotBlank
    @Schema(description = "유저 아이디", example = "sp")
    private String loginId;
    @NotBlank
    @Schema(description = "유저 비밀번호", example = "1234")
    private String password;

}
