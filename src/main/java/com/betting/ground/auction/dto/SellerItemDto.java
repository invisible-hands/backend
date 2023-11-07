package com.betting.ground.auction.dto;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.Duration;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SellerItemDto {
    @Schema(description = "경매글 아이디", example = "15")
    private Long auctionId;
    @Schema(description = "경매글 제목", example = "초이가 말아주는 버블티")
    private String title;
    @Schema(description = "현재 입찰 가격", example="175000")
    private Long currentPrice;
    @Schema(description = "경매글 사진 이메일 첫 장", example = "image-s3-url-15")
    private String imageUrl;
    @Schema(description = "경매 등록 시간", example="2023-10-20 13:35:10")
    private LocalDateTime createdAt;
    @Schema(description = "경매 시작 시간", example="2023-10-20 13:40:10") // 2023-10-20 13:40:10
    private LocalDateTime auctionStartTime;
    @Schema(description = "경매 기간", example="24")
    private int duration;
    @Schema(description = "경매 종료 시간", example="2023-10-20 13:35:10")
    private LocalDateTime endAuctionTime;
    @Schema(description = "경매 상태", example="2023-10-20 13:35:10")
    private String auctionStatus;

    @Builder
    public SellerItemDto(Long auctionId, String title, Long currentPrice, String imageUrl, LocalDateTime createdAt, Duration duration, LocalDateTime endAuctionTime, AuctionStatus auctionStatus) {
        this.auctionId = auctionId;
        this.title = title;
        this.currentPrice = currentPrice;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.auctionStartTime = createdAt.plusMinutes(5L);
        this.duration = duration.getTime();
        this.endAuctionTime = endAuctionTime;
        this.auctionStatus = auctionStatus.name();
    }
}
