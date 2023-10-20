package com.betting.ground.auction.domain;

import lombok.Getter;

@Getter

public enum AuctionStatus {

    AUCTION_PROGRESS("경매진행중"),
    AUCTION_FINISH("경매종료"),
    DELIVERY_WAITING("배송대기중"),
    PURCHASE_PROGRESS("구매확정대기중"),
    PURCHASE_COMPLETE("구매확정"),
    PURCHASE_CANCEL("취소");

    private String status;

    AuctionStatus(String status) {
        this.status = status;
    }
}


