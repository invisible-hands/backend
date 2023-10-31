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
    private Long bidderId;
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

    @Builder
    public Auction(String title, String content, ItemCondition itemCondition, Long startPrice, Long instantPrice, Long currentPrice, AuctionStatus auctionStatus, Duration duration, LocalDateTime endAuctionTime, LocalDateTime createdAt, LocalDateTime updatedAt, User user) {
        this.title = title;
        this.content = content;
        this.itemCondition = itemCondition;
        this.startPrice = startPrice;
        this.instantPrice = instantPrice;
        this.currentPrice = currentPrice;
        this.auctionStatus = auctionStatus;
        this.duration = duration;
        this.endAuctionTime = endAuctionTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
    }

    public void calcEndAuctionTime(int duration) {
        this.endAuctionTime = this.createdAt.plusHours(duration);
    }

    @Override
    public String toString() {
        return "Auction{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", itemCondition=" + itemCondition +
                ", startPrice=" + startPrice +
                ", instantPrice=" + instantPrice +
                ", currentPrice=" + currentPrice +
                ", auctionStatus=" + auctionStatus +
                ", duration=" + duration +
                ", endAuctionTime=" + endAuctionTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public void updateViewCnt() {
        this.viewCnt++;
    }

    public void updateAuctionStatus(AuctionStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }

    public void udpateBid(Long bidderId, Long price, AuctionStatus auctionStatus) {
        this.bidderId = bidderId;
        this.currentPrice = price;
        this.auctionStatus = auctionStatus;
    }
}
