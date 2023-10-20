package com.betting.ground.user.dto;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserAccountDTO {
    //계좌번호 등록,수정
    @Schema(description = "계좌번호", example = "4321-0987-6543")
    private String accountNumber;
}
