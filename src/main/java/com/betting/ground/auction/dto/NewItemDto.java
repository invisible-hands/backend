package com.betting.ground.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class NewItemDto {
    @Schema(description = "경매 id", example="1")
    private Long auctionId;
    @Schema(description = "상품 이름", example="아이폰15 pro")
    private String title;
    @Schema(description = "상품 이미지들")
    private List<AuctionImageDto> auctionImgUrl;
    @Schema(description = "현재 경매가", example="300000")
    private Long currentPrice;
    @Schema(description = "즉시 거래가", example="500000")
    private Long instantPrice;
    @Schema(description = "경매 시작시간", example = "2023-10-20 13:50:10")
    private String auctionStartTime;
    @Schema(description = "경매 기간", example="24")
    private int duration;
    @Schema(description = "경매 상태", example="경매 진행중")
    private String auctionStatus;
}