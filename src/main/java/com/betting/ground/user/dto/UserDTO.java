package com.betting.ground.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    //프로필조회
    @NotBlank
    @Schema(description = "닉네임", example = "김경매123")
    private String nickname;

    @Schema(description = "프로필 사진", example = "이미지")
    private String profileImage;

    @NotBlank
    @Schema(description = "은행명", example = "국민은행")
    private String bankName;

    @NotBlank
    @Schema(description = "계좌번호", example = "1234-5678-9101")
    private String bankAccount;

    @NotBlank
    @Schema(description = "도로명주소", example = "서울특별시 양천구 목동중앙북로 1길")
    private String roadName;

    @NotBlank
    @Schema(description = "지번주소", example = "서울특별시 양천구 123-4")
    private String addressName;

    @NotNull
    @Schema(description = "우편번호", example = "19524")
    private Integer zipcode;

    @NotBlank
    @Schema(description = "상세주소", example = "1011호")
    private String detailAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "가입일", example = "2023-10-20")
    private LocalDateTime registerDate;

    @Schema(description = "가상머니", example = "20000")
    private Long money;

    @Email
    @NotBlank
    @Schema(description = "이메일", example = "betting@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "권한", example = "role")
    private String role;

}