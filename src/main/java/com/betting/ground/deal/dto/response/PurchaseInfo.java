package com.betting.ground.deal.dto.response;

import java.time.LocalDateTime;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionImage;
import com.betting.ground.auction.domain.Duration;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseInfo {

	@Schema(description = "상품 아이디", example = "1")
	private Long auctionId;
	@Schema(description = "거래 아이디", example = "12")
	private Long dealId;
	@Schema(description = "상품 이미지", example = "https://~~~")
	private String imageUrl;
	@Schema(description = "상품명", example = "최하록이 만든 마법의 아이폰 25 mini")
	private String title;
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
	@Schema(description = "낙찰가격", example = "500")
	private Long purchasePrice;
	@Schema(description = "상태", example = "DELIVERY_WAITING")
	private String status;

	@QueryProjection
	public PurchaseInfo(Long auctionId, Long dealId, String imageUrl, String title, LocalDateTime createdAt,
		Duration duration, LocalDateTime endAuctionTime, Long purchasePrice, DealStatus status) {
		this.auctionId = auctionId;
		this.dealId = dealId;
		this.imageUrl = imageUrl;
		this.title = title;
		this.createdAt = createdAt;
		this.duration = duration.getTime();
		this.startAuctionTime = createdAt.plusMinutes(5L);
		this.endAuctionTime = endAuctionTime;
		this.purchasePrice = purchasePrice;
		this.status = status.name();
	}

	public PurchaseInfo(Deal deal, Auction auction, AuctionImage image) {
		this.auctionId = auction.getId();
		this.dealId = deal.getId();
		this.imageUrl = image.getImageUrl();
		this.title = auction.getTitle();
		this.createdAt = auction.getCreatedAt();
		this.duration = auction.getDuration().getTime();
		this.startAuctionTime = auction.getCreatedAt().plusMinutes(5L);
		this.endAuctionTime = deal.getDealTime();
		this.purchasePrice = deal.getDealPrice();
		this.status = deal.getDealStatus().name();
	}
}
