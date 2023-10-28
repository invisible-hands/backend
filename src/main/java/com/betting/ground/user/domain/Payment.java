package com.betting.ground.user.domain;

import com.betting.ground.kakaopay.dto.response.KakaoApproveResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    private Long money;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Payment(KakaoApproveResponse response, User user) {
        this.money = response.getAmount().getTotal();
        this.paymentType = PaymentType.IN;
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }
}
