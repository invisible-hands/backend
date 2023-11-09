package com.betting.ground.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRequestDTO {

    @NotNull(message = "해당 신고건의 id 누락")
    @Schema(description = "신고 id", example = "1")
    private Long id;

}
