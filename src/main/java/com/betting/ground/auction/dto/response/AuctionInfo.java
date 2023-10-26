package com.betting.ground.auction.dto.response;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.Duration;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class AuctionInfo {

    @Schema(description = "경매 아이디", example = "1")
    private Long auctionId;
    @Schema(description = "물품 제목", example = "내가 만든 쿠키")
    private String title;
    @Schema(description = "현재 가격", example = "8000")
    private Long currentPrice;
    @Schema(description = "즉시구매 가격", example = "12000")
    private Long instantPrice;
    @Schema(description = "경매 시작 시각 => 경매등록시간+5분", example = "2023-10-24 09:00:01")
    private String auctionStartTime;
    @Schema(description = "경매 기간", example = "6")
    private int duration;
    @Schema(description = "경매 상태", example = "경매진행중")
    private String auctionStatus;
    @Schema(description = "조회수", example = "30")
    private int viewCnt;
    @Schema(description = "이미지 url", example = "aws-s3-url-14")
    private String imageUrl;

    public AuctionInfo(Long auctionId, String title, Long currentPrice, Long instantPrice, LocalDateTime auctionStartTime,LocalDateTime endAuctionTime, Duration duration, int viewCnt, String imageUrl) {
        this.auctionId = auctionId;
        this.title = title;
        this.currentPrice = currentPrice;
        this.instantPrice = instantPrice;
        this.auctionStartTime = auctionStartTime.plusMinutes(5L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.duration = duration.getTime();
        this.viewCnt = viewCnt;
        this.auctionStatus = endAuctionTime.isBefore(LocalDateTime.now()) ?
                "경매 종료" : AuctionStatus.AUCTION_PROGRESS.getStatus();
        this.imageUrl = imageUrl;
    }
}
