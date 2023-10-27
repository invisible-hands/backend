package com.betting.ground.deal.domain;

import com.betting.ground.auction.domain.Auction;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    private Long buyerId;
    private Long dealPrice;
    private LocalDateTime dealTime;
    private LocalDateTime dealDeadLine;
    @Enumerated(EnumType.STRING)
    private DealStatus dealStatus;

    @OneToOne(fetch = FetchType.LAZY)
    private Auction auction;

    public Deal(Auction auction) {
        this.sellerId = auction.getUser().getId();
        this.buyerId = auction.getBidderId();
        this.dealPrice = auction.getCurrentPrice();
        this.dealTime = auction.getEndAuctionTime();
        this.dealStatus = DealStatus.DELIVERY_WAITING;
        this.dealDeadLine = auction.getBidderId() == null ? null : auction.getEndAuctionTime().plusHours(48L);
        this.auction = auction;
    }
}
