package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "닉네임을 입력해 주세요. (공백은 허용되지 않습니다)")
    @Size(min = 1, max = 12, message = "닉네임의 최대길이는 12자 입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 영어, 숫자, 한글만 포함할 수 있습니다.")
    @Schema(description = "닉네임", example = "최경매321")
    private String nickname;
}
