package com.betting.ground.auction.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Auction auction;

    public Tag(String tagName, Auction auction) {
        this.tagName = tagName;
        this.auction = auction;
    }
}
