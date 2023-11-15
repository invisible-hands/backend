package com.betting.ground.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class ReportRequestDto {

    @NotNull(message = "해당 신고건의 id가 누락되었습니다.")
    @Schema(description = "신고 id", example = "12")
    private Long id;

}
