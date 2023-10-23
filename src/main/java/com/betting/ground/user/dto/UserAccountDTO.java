package com.betting.ground.user.dto;
import java.time.LocalDate;

import com.betting.ground.user.domain.Bank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserAccountDTO {
    //계좌번호 등록,수정
    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @Schema(description = "계좌번호", example = "1234-5678-9101")
    private String bankAccount;
}
