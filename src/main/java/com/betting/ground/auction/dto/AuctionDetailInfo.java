package com.betting.ground.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AuctionDetailInfo {
    @Schema(description = "경매 id", example = "1")
    private Long auctionId;
    @Schema(description = "판매자 id", example = "1")
    private Long sellerId;
    @Schema(description = "제목", example = "아이패드 에어4")
    private String title;
    @Schema(description = "상품 설명", example = "미개봉 새 제품, 스카이블루")
    private String content;
    @Schema(description = "상품 상태", example = "NEW")
    private String itemCondition;
    @Schema(description = "현재 입찰 가격", example = "175000")
    private Long currentPrice;
    @Schema(description = "즉시 구매 가격", example = "200000")
    private Long instantPrice;
    @Schema(description = "경매 등록 시간", example = "2023-10-20 13:35:10")
    private String createdAt;
    @Schema(description = "경매 종료 시간", example = "2023-10-20 13:40:10") // 2023-10-20 13:40:10
    private String endAuctionTime;
    @Schema(description = "경매 기간", example = "24")
    private int duration;
    @Schema(description = "참여자 수", example = "8")
    private Long bidderCnt;
    @Schema(description = "조회수", example = "32")
    private int viewCnt;
}
