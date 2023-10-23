package com.betting.ground.user.dto;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class UserNameDTO {
    //닉네임수정
    @Schema(description = "닉네임", example = "김경매321")
    private String nickname;
}
