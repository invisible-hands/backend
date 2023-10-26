package com.betting.ground.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequestDto {
    @NotBlank
    private String refreshToken;
}