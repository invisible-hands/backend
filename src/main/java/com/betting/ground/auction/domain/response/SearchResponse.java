package com.betting.ground.auction.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    private List<AuctionInfo> content;
    @Schema(description = "전체 페이지 수", example = "10")
    private Integer totalPages;
    @Schema(description = "현재 페이지", example = "1")
    private Integer currentPage;
}
