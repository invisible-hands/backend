package com.betting.ground.auction.controller;

import com.betting.ground.auction.dto.ItemResponse;
import com.betting.ground.auction.service.AuctionService;
import com.betting.ground.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
@Tag(name = "경매", description = "경매 관련 api")
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/new")
    @Operation(summary = "메인페이지", description = "새로 들어온 제품 (최신순)")
    public Response<ItemResponse> getNewItem(
            @Parameter(
                    description = "page, size만 주시면 됩니다!! ex) ?page=1&size=10"
            ) Pageable pageable) {
        return Response.success("새로 들어온 제품", auctionService.getNewItem(pageable));
    }

    @GetMapping("/deadline")
    @Operation(summary = "메인페이지", description = "마감 임박한 상품")
    public Response<ItemResponse> getDeadline(
            @Parameter(
                    description = "page, size만 주시면 됩니다!! ex) ?page=1&size=10"
            ) Pageable pageable) {
        return Response.success("마감 임박한 상품", auctionService.getDeadline(pageable));
    }

    @GetMapping("/view")
    @Operation(summary = "메인페이지", description = "조회수 많은 상품")
    public Response<ItemResponse> getMostView(
            @Parameter(
                    description = "page, size만 주시면 됩니다!! ex) ?page=1&size=10"
            ) Pageable pageable) {
        return Response.success("조회수 많은 상품", auctionService.getMostView(pageable));
    }
}
