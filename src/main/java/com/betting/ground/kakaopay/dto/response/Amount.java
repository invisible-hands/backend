package com.betting.ground.kakaopay.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Amount {

    @Schema(description = "총 결제 금액", example = "10000")
    private Long total;
    @Schema(description = "비과세 금액", example = "0")
    private int tax_free;
    @Schema(description = "부과세 금액", example = "0")
    private int tax;
    @Schema(description = "사용한 포인트", example = "2000")
    private int point;
    @Schema(description = "할인 금액", example = "0")
    private int discount;
    @Schema(description = "컵 보증금", example = "0")
    private int green_deposit;
}