package com.betting.ground.auction.dto.response;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.dto.AuctionImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BidInfoResponse {

    @Schema(description = "상품 이미지")
    private List<AuctionImageDto> images;
    @Schema(description = "상품명", example="최진영이 말아준 버블티")
    private String title;
    @Schema(description = "현재 가격", example="20000")
    private Long currentPrice;
    @Schema(description = "경매 종료 시각", example="2023-10-14 19:49:00")
    private String endAuctionTime;
    @Schema(description = "보유 포인트", example="10000")
    private Long money;
    @Schema(description = "판매자 구매자 일치 여부", example = "false")
    private Boolean isAuthor;
}
