package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAddressDto {
    //주소 등록,수정
    @NotNull(message = "도로명주소를 입력해주세요.")
    @Schema(description = "도로명주소", example = "서울특별시 양천구 목동중앙북로 1길")
    private String roadName;

    @NotNull(message = "지번주소를 입력해주세요.")
    @Schema(description = "지번주소", example = "서울특별시 양천구 123-4")
    private String addressName;

    @NotNull(message = "우편번호를 입력해주세요.")
    @Schema(description = "우편번호", example = "19524")
    private Integer zipcode;

    @NotNull(message = "상세주소를 입력해주세요.")
    @Schema(description = "상세주소", example = "1011호")
    private String detailAddress;
}