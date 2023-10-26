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

    public void updateStatus(DealStatus status){
        this.dealStatus = status;
    }
}
