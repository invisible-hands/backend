package com.betting.ground.auction.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    @Schema(description = "경매 시작 시각", example = "2023-10-24 09:00:01")
    private String auctionStartTime;
    @Schema(description = "경매 기간", example = "6")
    private String duration;
    @Schema(description = "이미지 주소", example = "https://~~~")
    private String imageUrl;
    @Schema(description = "경매 상태", example = "경매 진행 중")
    private String auctionStatus;
}
