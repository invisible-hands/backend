package com.betting.ground.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class BidInfoResponse {

    @Schema(description = "상품 이미지", example = "https://~~")
    private String image;
    @Schema(description = "상품명", example="최진영이 말아준 버블티")
    private String title;
    @Schema(description = "현재 가격", example="20000")
    private Long currentPrice;
    @Schema(description = "경매 종료 시각", example="2023-10-14 19:49:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endAuctionTime;
    @Schema(description = "보유 포인트", example="10000")
    private Long money;

    public BidInfoResponse(String image, String title, Long currentPrice, LocalDateTime endAuctionTime) {
        this.image = image;
        this.title = title;
        this.currentPrice = currentPrice;
        this.endAuctionTime = endAuctionTime;
    }
}
