package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "은행명을 입력해 주세요.")
    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @NotBlank(message = "계좌번호를 입력해 주세요.")
    @Schema(description = "계좌번호", example = "1234-5678-9101")
    private String bankAccount;
}
