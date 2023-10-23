package com.betting.ground.auction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private List<AuctionInfo> newItems;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "총 페이지", example="4")
    private int totalPage;
}
