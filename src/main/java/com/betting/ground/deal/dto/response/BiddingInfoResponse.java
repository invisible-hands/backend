package com.betting.ground.deal.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
@NoArgsConstructor
public class BiddingInfoResponse {

    @Schema(description = "상품 정보")
    private List<BiddingInfo> auctions;
    @Schema(description = "게시물 개수", example="4")
    private long cnt;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "총 페이지", example="4")
    private int totalPage;

    public BiddingInfoResponse(PageImpl<BiddingInfo> page) {
        this.auctions = page.getContent();
        this.cnt = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.totalPage = page.getTotalPages();
    }
}
