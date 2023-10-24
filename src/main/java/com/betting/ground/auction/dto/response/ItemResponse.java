package com.betting.ground.auction.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemResponse {
    private List<AuctionInfo> items;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "총 페이지", example="4")
    private int totalPage;

    public ItemResponse(PageImpl<AuctionInfo> newItems) {
        this.items = newItems.getContent();
        this.currentPage = newItems.getNumber();
        this.totalPage = newItems.getTotalPages();
    }
}
