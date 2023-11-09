package com.betting.ground.auction.domain;

import com.betting.ground.auction.dto.request.AuctionCreateRequest;
import com.betting.ground.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

import static com.betting.ground.auction.domain.AuctionStatus.AUCTION_PROGRESS;

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void updateAuctionStatus(AuctionStatus auctionStatus) {
        this.auctionStatus = auctionStatus;
    }


    public boolean hasBidder(){
        return !(bidderId == null && (this.currentPrice == this.startPrice));
    }

    public void updateBid(Long bidderId, Long price, AuctionStatus auctionStatus) {
        this.bidderId = bidderId;
        this.currentPrice = price;
        this.auctionStatus = auctionStatus;
    }

    @Builder
    public Auction(String title, String content, ItemCondition itemCondition, Long startPrice, Long instantPrice, Duration duration, User user) {
        this.title = title;
        this.content = content;
        this.itemCondition = itemCondition;
        this.startPrice = startPrice;
        this.instantPrice = instantPrice;
        this.duration = duration;
        this.user = user;

        this.currentPrice = startPrice;
        this.auctionStatus = AUCTION_PROGRESS;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.endAuctionTime = LocalDateTime.now().plusMinutes(5L + duration.getTime());
    }
}
