package com.betting.ground.auction.dto.response;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionImage;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.Duration;
import com.betting.ground.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class BidInfoResponse {

    @Schema(description = "상품 이미지", example = "https://~~")
    private String image;
    @Schema(description = "상품명", example="최진영이 말아준 버블티")
    private String title;
    @Schema(description = "현재 가격", example="20000")
    private Long currentPrice;
    @Schema(description = "경매 생성 시각", example="2023-10-13 19:49:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @Schema(description = "경매 지속 시간", example = "DAY")
    private int duration;
    @Schema(description = "경매 시작 시각", example="2023-10-13 19:54:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startAuctionTime;
    @Schema(description = "경매 종료 시각", example="2023-10-14 19:49:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAuctionTime;
    @Schema(description = "경매 상태", example="AUCTION_PROGRESS")
    private AuctionStatus status;
    @Schema(description = "유저 보유 포인트", example="10000")
    private Long money;

    public BidInfoResponse(AuctionImage image, Auction auction, User user) {
        this.image = image == null ? null : image.getImageUrl();
        this.title = auction.getTitle();
        this.currentPrice = auction.getCurrentPrice();
        this.createdAt = auction.getCreatedAt();
        this.duration = auction.getDuration().getTime();
        this.startAuctionTime = auction.getCreatedAt().plusMinutes(5L);
        this.endAuctionTime = auction.getEndAuctionTime();
        this.status = auction.getAuctionStatus();
        this.money = user.getMoney();
    }
}
