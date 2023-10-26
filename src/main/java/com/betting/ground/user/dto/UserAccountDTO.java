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
public class UserAccountDTO {
    //계좌번호 등록,수정
    @Email
    @NotNull
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @Schema(description = "계좌번호", example = "1234-5678-9101")
    private String bankAccount;
}
