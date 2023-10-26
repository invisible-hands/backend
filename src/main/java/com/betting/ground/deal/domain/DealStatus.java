package com.betting.ground.deal.domain;

import lombok.Getter;

@Getter
public enum DealStatus {
    DELIVERY_WAITING("배송 대기중"),
    PURCHASE_COMPLETE_WAITING("구매확정 대기"),
    PURCHASE_COMPLETE("구매확정"),
    PURCHASE_CANCEL("취소");

    private String status;

    DealStatus(String status) {
        this.status = status;
    }
}
