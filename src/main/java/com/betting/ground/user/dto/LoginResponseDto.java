package com.betting.ground.user.dto;

import com.betting.ground.RefreshToken.domain.RefreshToken;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
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

    public LoginResponseDto(User user, String accessToken, String refreshToken) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole().toString();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}