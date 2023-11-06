package com.betting.ground.auction.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayRequest {
    private Long price;

    public PayRequest(Long price) {
        this.price = price;
    }
}
