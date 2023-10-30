package com.betting.ground.user.dto;

import com.betting.ground.user.domain.Role;
import com.betting.ground.user.dto.login.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder @AllArgsConstructor
public class LoginResponseDto {
    @Schema(description = "유저 아이디", example="33")
    private Long userId;
    @Schema(description = "이메일", example="panjun@naver.com")
    private String email;
    @Schema(description = "이름", example="권판준")
    private String username;
    @Schema(description = "닉네임", example="권판준(1489233)")
    private String nickname;
    @Schema(description = "권한", example="GUEST")
    private String role;
    @Schema(description = "엑세스 토큰", example="")
    private String accessToken;
    @Schema(description = "리프레시 토큰", example="")
    private String refreshToken;
}