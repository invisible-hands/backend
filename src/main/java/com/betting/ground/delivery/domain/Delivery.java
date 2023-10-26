package com.betting.ground.delivery.domain;

import com.betting.ground.auction.domain.Auction;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoice;
    private String deliveryCompany;

    @OneToOne(fetch = FetchType.LAZY)
    private Auction auction;
}
