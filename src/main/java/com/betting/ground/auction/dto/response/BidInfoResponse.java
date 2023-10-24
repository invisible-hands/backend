package com.betting.ground.auction.dto.response;

import com.betting.ground.auction.dto.AuctionImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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

    public BidInfoResponse(String title, Long currentPrice, LocalDateTime endAuctionTime, Long money) {
        this.images = new ArrayList<>();
        this.title = title;
        this.currentPrice = currentPrice;
        this.endAuctionTime = endAuctionTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.money = money;
    }
}
