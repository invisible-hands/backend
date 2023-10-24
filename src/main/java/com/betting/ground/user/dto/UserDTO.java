package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {

    //프로필조회
    @Schema(description = "닉네임", example = "김경매123")
    private String nicknameInfo;

    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @Schema(description = "계좌번호", example = "1234-5678-9101")
    private String bankAccount;

    @Schema(description = "도로명주소", example = "서울특별시 양천구 목동중앙북로 1길")
    private String roadName;

    @Schema(description = "지번주소", example = "서울특별시 양천구 123-4")
    private String addressName;

    @Schema(description = "우편번호", example = "19524")
    private String zipcode;

    @Schema(description = "상세주소", example = "1011호")
    private String detailAddress;

    @Schema(description = "가입일", example = "2023-10-20")
    private LocalDateTime registerDate;

    @Schema(description = "가상머니", example = "20000")
    private String money;

    @Schema(description = "이메일", example = "betting@gmail.com")
    private String emailAddress;

    @Schema(description = "권한", example = "guest")
    private String role;

}
