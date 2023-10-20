package com.betting.ground.user.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.betting.ground.user.domain.Address;
import com.betting.ground.user.domain.Bank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    //프로필조회
    @Schema(description = "닉네임", example = "김경매123")
    private String nicknameInfo;

    @Schema(description = "계좌번호", example = "1234-5678-9101")
    private Bank accountNumberInfo;

    @Schema(description = "주소", example = "서울특별시 양천구")
    private Address addressInfo;

    @Schema(description = "가입일", example = "2023-10-20")
    private LocalDateTime registerDate;

    @Schema(description = "가상머니", example = "20000")
    private String money;

    @Schema(description = "이메일", example = "betting@gmail.com")
    private String emailAddress;


}
