package com.betting.ground.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseStatusCnt {

    @Schema(description = "전체", example = "12")
    private int all;
    @Schema(description = "배송중", example = "4")
    private int shipping;
    @Schema(description = "구매확정 대기", example = "4")
    private int progress;
    @Schema(description = "거래 완료", example = "4")
    private int finish;
}
