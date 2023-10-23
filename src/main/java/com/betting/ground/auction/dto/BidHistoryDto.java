package com.betting.ground.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class BidHistoryDto {
    @Schema(description = "입찰 내역", example="")
    private List<BidInfo> bids;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "전체 페이지", example="3")
    private int totalPage;
}
