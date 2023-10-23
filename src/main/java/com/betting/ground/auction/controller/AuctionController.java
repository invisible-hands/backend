package com.betting.ground.auction.controller;

import com.betting.ground.auction.domain.dto.BidHistoryDto;
import com.betting.ground.auction.domain.dto.BidInfo;
import com.betting.ground.auction.domain.dto.ItemDetailDto;
import com.betting.ground.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {
    @GetMapping("/{auctionId}")
    @Operation(summary = "경매 상세 정보", description = "")
    @Parameter(name = "auctionId", description = "경매글 아이디", example = "1")
    public Response<ItemDetailDto> getItemDetail(@PathVariable Long auctionId) {
        return Response.success("해당 경매글 보기 성공", new ItemDetailDto());
    }

    @GetMapping("/{auctionId}/bidHistory")
    @Operation(summary = "입찰 내역", description = "")
    @Parameter(name = "auctionId", description = "경매글 아이디", example = "4")
    public Response<BidHistoryDto> getBidHistory(@PathVariable Long auctionId) {
        return Response.success("해당 경매글의 입찰 내역 보기 성공", new BidHistoryDto());
    }

    @GetMapping("/{auctionId}/seller")
    @Operation(summary = "판매자 정보", description = "")
    @Parameter(name = "auctionId", description = "경매글 아이디", example = "4")
    public Response<SellerInfo> getSeller(@PathVariable Long auctionId) {
        return Response.success("해당 경매글의 판매자 정보 보기 성공", new SellerInfo());
    }

    @PostMapping("/{auctionId}/instant")
    @Operation(summary = "즉시 결제", description = "")
    @Parameter(name = "auctionId", description = "경매글 아이디", example = "4")
    public Response<Void> instantBuy(@PathVariable Long auctionId) {
        return Response.success("해당 경매글의 즉시 결제 성공", null);
    }
}
