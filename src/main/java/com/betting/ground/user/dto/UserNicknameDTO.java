package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class UserNicknameDTO {
    //닉네임수정
    @Email
    @NotNull
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "닉네임", example = "최경매321")
    private String nickname;
}
