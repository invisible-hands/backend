package com.betting.ground.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class biddingItemDto {
    @Schema(description = "경매글 아이디", example = "15")
    private Long auctionId;
    @Schema(description = "경매글 제목", example = "초이가 말아주는 버블티")
    private String title;
    @Schema(description = "현재 입찰 가격", example="175000")
    private Long nowPrice;
    @Schema(description = "경매글 사진 이메일 첫 장", example = "image-s3-url-15")
    private String imageUrl;
    @Schema(description = "경매 등록 시간", example="2023-10-20 13:35:10")
    private String createdAt;
    @Schema(description = "경매 시작 시간", example="2023-10-20 13:40:10") // 2023-10-20 13:40:10
    private String auctionStartTime;
    @Schema(description = "경매 기간", example="24")
    private int duration;
}
