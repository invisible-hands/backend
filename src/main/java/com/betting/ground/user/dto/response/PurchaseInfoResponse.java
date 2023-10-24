package com.betting.ground.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PurchaseInfoResponse {

    @Schema(description = "경매 정보")
    private List<PurchaseInfo> auctions;
    @Schema(description = "상태 개수")
    private PurchaseStatusCnt cnt;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "총 페이지", example="4")
    private int totalPage;
}
