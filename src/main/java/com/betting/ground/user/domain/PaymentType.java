package com.betting.ground.user.domain;

import lombok.Getter;

@Getter
public enum PaymentType {
    IN_CHARGE("충전"),
    OUT_EXCHANGE("환전"),
    OUT_BID("입찰"),
    IN_CANCEL("입찰취소"),
    IN_SETTLEMENT("판매대금정산")
    ;

    private String name;

    PaymentType(String name) {
        this.name = name;
    }
}
