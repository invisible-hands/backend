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
public class UserAddressDTO {

    //주소 등록,수정
    @Email
    @NotNull
    @Schema(description = "이메일", example = "test@naver.com")
    private String email;

    @Schema(description = "도로명주소", example = "서울특별시 양천구 목동중앙북로 1길")
    private String roadName;

    @Schema(description = "지번주소", example = "서울특별시 양천구 123-4")
    private String addressName;

    @Schema(description = "우편번호", example = "19524")
    private String zipcode;

    @Schema(description = "상세주소", example = "1011호")
    private String detailAddress;

}

