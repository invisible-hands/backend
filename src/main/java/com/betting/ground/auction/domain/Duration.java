package com.betting.ground.auction.domain;

import lombok.Getter;

@Getter
public enum Duration {
    QUARTER(6),HALF(12),DAY(24);

    private int time;

    Duration(int time) {
        this.time = time;
    }
}
