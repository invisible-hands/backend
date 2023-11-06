package com.betting.ground.auction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bidderId;
    private String nickname;
    private LocalDateTime createdAt;
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Auction auction;

    public BidHistory(Long bidderId, String nickname, LocalDateTime createdAt, Long price, Auction auction) {
        this.bidderId = bidderId;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.price = price;
        this.auction = auction;
    }
}
