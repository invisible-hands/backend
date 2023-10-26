package com.betting.ground.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
@NoArgsConstructor
public class PurchaseInfoResponse {

    @Schema(description = "경매 정보")
    private List<PurchaseInfo> auctions;
    @Schema(description = "게시물 개수", example="4")
    private int cnt;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "총 페이지", example="4")
    private int totalPage;
    @Schema(description = "게시글 수", example = "8")
    private long count;

    public PurchaseInfoResponse(PageImpl<PurchaseInfo> page) {
        this.auctions = page.getContent();
        this.currentPage = page.getNumber();
        this.totalPage = page.getTotalPages();
        this.cnt = page.getTotalElements();
    }
}
