package com.betting.ground.auction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;
    private Long buyerId;
    private Long finalPrice;
    private LocalDateTime closeAuctionTime;
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @OneToOne(fetch = FetchType.LAZY)
    private Auction auction;
}
