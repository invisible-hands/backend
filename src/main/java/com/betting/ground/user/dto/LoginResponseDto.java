package com.betting.ground.user.dto;

import com.betting.ground.user.domain.Role;
import com.betting.ground.user.dto.login.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder @AllArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String email;
    private String username;
    private String nickname;
    private String role;
    private String accessToken;
    private String refreshToken;
}