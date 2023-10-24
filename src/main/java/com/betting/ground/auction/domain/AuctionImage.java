package com.betting.ground.auction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Auction auction;


    public AuctionImage(String imageUrl, Auction auction) {
        this.imageUrl = imageUrl;
        this.auction = auction;
    }
}
