package com.betting.ground.auction.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCreateRequest {
    @Schema(description = "물품 제목", example = "내가 만든 쿠키")
    private String title;
    @Schema(description = "물품 설명", example = "뉴진스가 아니라 제가 구운 쿠키 전혀 건강을 생각하지 않아 버터를 때려박았어요")
    private String content;
    @Schema(description = "새제품/중고제품 구분", example = "NEW")
    private String itemCondition;
    @Schema(description = "경매 시작가", example = "10000")
    private Long startPrice;
    @Schema(description = "즉시 판매가", example = "25000")
    private Long instantPrice;
    @Schema(description = "경매 시간", example = "DAY", allowableValues = {"QUARTER", "HALF", "DAY"})
    private String duration;
    @Schema(description = "태그", example = "")
    private List<String> tags;
}
