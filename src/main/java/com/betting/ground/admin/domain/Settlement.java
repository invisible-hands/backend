package com.betting.ground.admin.domain;

import com.betting.ground.deal.domain.Deal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;
    private Long sellerId;
    private LocalDateTime createdAt;

    public Settlement(Deal deal) {
        this.price = deal.getDealPrice();
        this.sellerId = deal.getSellerId();
        this.createdAt = LocalDateTime.now();
    }
}
