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
    @Schema(description = "주소", example = "서울특별시 용산구")
    private String address;
}
