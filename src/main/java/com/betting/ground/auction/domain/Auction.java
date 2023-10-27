package com.betting.ground.auction.domain;

import com.betting.ground.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE auction SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private ItemCondition itemCondition;
    private Long startPrice;
    private Long instantPrice;
    private Long currentPrice;
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;
    @Enumerated(EnumType.STRING)
    private Duration duration;
    private LocalDateTime endAuctionTime;
    private int viewCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void updateViewCnt() {
        this.viewCnt++;
    }
}
