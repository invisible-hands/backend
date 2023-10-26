package com.betting.ground.deal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SalesInfo {
    @Schema(description = "상품 아이디", example="1")
    private Long auctionId;
    @Schema(description = "거래 아이디", example = "12")
    private Long dealId;
    @Schema(description = "상품 이미지", example="https://~~~")
    private String imageUrl;
    @Schema(description = "상품명", example="최하록이 만든 마법의 아이폰 25 mini")
    private String title;
    @Schema(description = "거래 체결 시각", example = "2027-04-12 12:00:00")
    private String closeAuctionTime;
    @Schema(description = "판매 가격", example="500")
    private Long price;
    @Schema(description = "상태", example="정산 완료")
    private String status;
}
