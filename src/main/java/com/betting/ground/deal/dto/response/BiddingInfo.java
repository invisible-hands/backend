package com.betting.ground.deal.dto.response;

import java.time.LocalDateTime;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionImage;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.BidHistory;
import com.betting.ground.auction.domain.Duration;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BiddingInfo {

	@Schema(description = "상품 아이디", example = "1")
	private Long auctionId;
	@Schema(description = "상품 이미지", example = "https://~~~")
	private String imageUrl;
	@Schema(description = "상품명", example = "최하록이 만든 마법의 아이폰 25 mini")
	private String title;
	@Schema(description = "경매 참여 시각(마지막)", example = "2027-04-12 12:00:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime bidTime;
	@Schema(description = "경매 생성 시각", example = "2023-10-13 19:49:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@Schema(description = "경매 지속 시간", example = "DAY")
	private int duration;
	@Schema(description = "경매 시작 시각", example = "2023-10-13 19:54:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime startAuctionTime;
	@Schema(description = "경매 종료 시각", example = "2023-10-14 19:49:00")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime endAuctionTime;
	@Schema(description = "현재 입찰가", example = "500")
	private Long currentPrice;
	@Schema(description = "내 입찰 금액", example = "500")
	private Long myBidPrice;
	@Schema(description = "상태", example = "AUCTION_PROGRESS")
	private String status;

	@QueryProjection
	public BiddingInfo(Long auctionId, String imageUrl, String title, LocalDateTime bidTime, LocalDateTime createdAt,
		Duration duration, LocalDateTime endAuctionTime, Long currentPrice, Long myBidPrice, AuctionStatus status) {
		this.auctionId = auctionId;
		this.imageUrl = imageUrl;
		this.title = title;
		this.bidTime = bidTime;
		this.createdAt = createdAt;
		this.duration = duration.getTime();
		this.startAuctionTime = createdAt.plusMinutes(5L);
		this.endAuctionTime = endAuctionTime;
		this.currentPrice = currentPrice;
		this.myBidPrice = myBidPrice;
		this.status = status.name();
	}

	public BiddingInfo(Auction auction, AuctionImage image, BidHistory history) {
		this.auctionId = auction.getId();
		this.imageUrl = image.getImageUrl();
		this.title = auction.getTitle();
		this.bidTime = history.getCreatedAt();
		this.createdAt = auction.getCreatedAt();
		this.duration = auction.getDuration().getTime();
		this.startAuctionTime = auction.getCreatedAt().plusMinutes(5L);
		this.endAuctionTime = auction.getEndAuctionTime();
		this.currentPrice = auction.getCurrentPrice();
		this.myBidPrice = history.getPrice();
		this.status = auction.getAuctionStatus().name();
	}
}
