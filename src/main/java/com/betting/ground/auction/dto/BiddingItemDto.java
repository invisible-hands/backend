package com.betting.ground.auction.dto;

import com.betting.ground.auction.domain.Duration;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BiddingItemDto {
    @Schema(description = "경매글 아이디", example = "15")
    private Long auctionId;
    @Schema(description = "경매글 제목", example = "초이가 말아주는 버블티")
    private String title;
    @Schema(description = "현재 입찰 가격", example="175000")
    private Long currentPrice;
    @Schema(description = "경매글 사진 이메일 첫 장", example = "image-s3-url-15")
    private String imageUrl;
    @Schema(description = "경매 등록 시간", example="2023-10-20 13:35:10")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @Schema(description = "경매 시작 시간", example="2023-10-20 13:40:10") // 2023-10-20 13:40:10
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime auctionStartTime;
    @Schema(description = "경매 기간", example="24")
    private int duration;

    @Builder
    public BiddingItemDto(Long auctionId, String title, Long currentPrice, String imageUrl, LocalDateTime createdAt, Duration duration) {
        this.auctionId = auctionId;
        this.title = title;
        this.currentPrice = currentPrice;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.auctionStartTime = createdAt.plusMinutes(5L);
        this.duration = duration.getTime();
    }
}