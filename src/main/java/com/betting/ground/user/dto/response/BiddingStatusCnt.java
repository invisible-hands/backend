package com.betting.ground.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BiddingStatusCnt {

    @Schema(description = "전체", example = "12")
    private int all;
    @Schema(description = "경매 진행", example = "8")
    private int progress;
    @Schema(description = "경매 종료", example = "4")
    private int finish;
}
