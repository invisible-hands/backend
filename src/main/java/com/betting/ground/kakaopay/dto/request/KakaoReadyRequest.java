package com.betting.ground.kakaopay.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoReadyRequest {

    @Schema(description = "결제 요청 금액", example = "10000")
    private Long price;
}
