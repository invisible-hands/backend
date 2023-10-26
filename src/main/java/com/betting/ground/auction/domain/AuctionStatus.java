package com.betting.ground.auction.domain;

import lombok.Getter;

@Getter
public enum AuctionStatus {

    AUCTION_PROGRESS("경매 진행중"),
    AUCTION_SUCCESS("낙찰 성공"),
    AUCTION_FAIL("낙찰 실패");

    private String status;

    AuctionStatus(String status) {
        this.status = status;
    }
}


