package com.betting.ground.user.domain;

import com.betting.ground.deal.domain.Deal;
import com.betting.ground.kakaopay.dto.response.KakaoApproveResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auctionId;
    private Long money;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Payment(KakaoApproveResponse response, User user) {
        this.money = response.getAmount().getTotal();
        this.paymentType = PaymentType.IN_CHARGE;
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }

    public Payment(Deal deal, PaymentType inSettlement, User user) {
        this.auctionId = deal.getAuction().getId();
        this.money = deal.getDealPrice();
        this.paymentType = inSettlement;
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }
}
