package com.betting.ground.auction.dto.response;

import java.time.LocalDateTime;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.Duration;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuctionInfo {

	@Schema(description = "경매 아이디", example = "1")
	private Long auctionId;
	@Schema(description = "물품 제목", example = "내가 만든 쿠키")
	private String title;
	@Schema(description = "현재 가격", example = "8000")
	private Long currentPrice;
	@Schema(description = "즉시구매 가격", example = "12000")
	private Long instantPrice;
	@Schema(description = "경매 기간", example = "12")
	private int duration;
	@Schema(description = "경매 등록 시각", example = "2023-10-24 09:00:01")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@Schema(description = "경매 시작 시각", example = "2023-10-24 09:00:01")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime startAuctionTime;
	@Schema(description = "경매 종료 시각", example = "2023-10-24 09:00:01")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime endAuctionTime;
	@Schema(description = "경매 상태", example = "AUCTION_PROGRESS")
	private String auctionStatus;
	@Schema(description = "조회수", example = "30")
	private int viewCnt;
	@Schema(description = "이미지 url", example = "aws-s3-url-14")
	private String imageUrl;

	public AuctionInfo(Long auctionId, String title, Long currentPrice, Long instantPrice, Duration duration,
		LocalDateTime createdAt, LocalDateTime endAuctionTime, AuctionStatus auctionStatus, int viewCnt,
		String imageUrl) {
		this.auctionId = auctionId;
		this.title = title;
		this.currentPrice = currentPrice;
		this.instantPrice = instantPrice;
		this.duration = duration.getTime();
		this.createdAt = createdAt;
		this.startAuctionTime = createdAt.plusMinutes(5L);
		this.endAuctionTime = endAuctionTime;
		this.auctionStatus = auctionStatus.name();
		this.viewCnt = viewCnt;
		this.imageUrl = imageUrl;
	}
}
