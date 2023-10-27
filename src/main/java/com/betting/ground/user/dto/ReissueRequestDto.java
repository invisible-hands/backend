package com.betting.ground.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequestDto {
    @NotBlank
    @Schema(description = "리프레시 토큰", example="")
    private String refreshToken;
}