package com.betting.ground.auction.domain;

import com.betting.ground.auction.dto.request.AuctionCreateRequest;
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
@Builder
@AllArgsConstructor
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

    public Auction(User user, AuctionCreateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.itemCondition = ItemCondition.valueOf(request.getItemCondition());
        this.startPrice = request.getStartPrice();
        this.instantPrice = request.getInstantPrice();
        this.currentPrice = request.getStartPrice();
        this.auctionStatus = AuctionStatus.AUCTION_PROGRESS;
        this.duration = Duration.valueOf(request.getDuration());
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.user = user;
    }

    public void calcEndAuctionTime(int duration) {
        this.endAuctionTime = this.createdAt.plusHours(duration);
    }

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

}
