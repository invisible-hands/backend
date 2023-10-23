package com.betting.ground.auction.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidRequest {
    @Schema(description = "경매 참여 금액", example = "1000")
    private int price;
}
