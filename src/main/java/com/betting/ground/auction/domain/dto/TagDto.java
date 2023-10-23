package com.betting.ground.auction.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TagDto {
    @Schema(description = "태그id", example="6")
    private Long tagId;
    @Schema(description = "태그명", example="#애플")
    private String tagName;
}
