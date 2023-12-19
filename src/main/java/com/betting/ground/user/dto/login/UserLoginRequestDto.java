package com.betting.ground.user.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginRequestDto {
    @Email(message = "email 형식이 올바르지 않습니다.")
    @Schema(description = "이메일", example="abc@naver.com")
    private String email;

    @Size(min = 4, max = 20, message = "password는 4자 ~ 20자 사이여야 합니다.")
    @Schema(description = "비밀번호", example="abcdef")
    private String password;
}
