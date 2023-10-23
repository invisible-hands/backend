package com.betting.ground.user.dto;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserAddressDTO {

    //주소 등록,수정
    @Schema(description = "도로명주소", example = "서울특별시 양천구 목동중앙북로 1길")
    private String roadName;

    @Schema(description = "지번주소", example = "서울특별시 양천구 123-4")
    private String addressName;

    @Schema(description = "우편번호", example = "19524")
    private String zipcode;

    @Schema(description = "상세주소", example = "1011호")
    private String detailAddress;

}
