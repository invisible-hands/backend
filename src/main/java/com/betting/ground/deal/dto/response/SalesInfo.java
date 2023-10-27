package com.betting.ground.deal.dto.response;

import com.betting.ground.deal.domain.DealStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime closeAuctionTime;
    @Schema(description = "송장번호 입력 마감 기한", example = "2027-04-12 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime dealDeadline;
    @Schema(description = "판매 가격", example="500")
    private Long price;
    @Schema(description = "상태", example="정산 완료")
    private String status;

    @QueryProjection
    public SalesInfo(Long auctionId, Long dealId, String imageUrl, String title, LocalDateTime closeAuctionTime, LocalDateTime dealDeadline, Long price, DealStatus status) {
        this.auctionId = auctionId;
        this.dealId = dealId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.closeAuctionTime = closeAuctionTime;
        this.dealDeadline = dealDeadline;
        this.price = price;
        this.status = status.getStatus();
    }
}
