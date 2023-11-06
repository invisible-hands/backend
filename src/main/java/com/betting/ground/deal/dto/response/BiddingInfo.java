package com.betting.ground.deal.dto.response;

import com.betting.ground.auction.domain.AuctionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BiddingInfo {

    @Schema(description = "상품 아이디", example="1")
    private Long auctionId;
    @Schema(description = "상품 이미지", example="https://~~~")
    private String imageUrl;
    @Schema(description = "상품명", example="최하록이 만든 마법의 아이폰 25 mini")
    private String title;
    @Schema(description = "경매 참여 시각(마지막)", example = "2027-04-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime bidTime;
    @Schema(description = "경매 종료 시각", example = "2027-04-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private LocalDateTime endAuctionTime;
    @Schema(description = "현재 입찰가", example="500")
    private Long currentPrice;
    @Schema(description = "내 입찰 금액", example="500")
    private Long myBidPrice;
    @Schema(description = "상태", example="AUCTION_PROGRESS")
    private String status;

    @QueryProjection
    public BiddingInfo(Long auctionId, String imageUrl, String title, LocalDateTime bidTime, LocalDateTime endAuctionTime, Long currentPrice, Long myBidPrice, AuctionStatus status) {
        this.auctionId = auctionId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.bidTime = bidTime;
        this.endAuctionTime = endAuctionTime;
        this.currentPrice = currentPrice;
        this.myBidPrice = myBidPrice;
        this.status = status.name();
    }
}
