package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력해 주세요.")
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 영어, 숫자, 한글만 포함할 수 있습니다.")
    @Schema(description = "닉네임", example = "최경매321")
    private String nickname;
}
