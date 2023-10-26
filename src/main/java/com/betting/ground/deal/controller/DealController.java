package com.betting.ground.deal.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.deal.dto.response.BiddingInfoResponse;
import com.betting.ground.deal.dto.response.DealCountResponse;
import com.betting.ground.deal.dto.response.PurchaseInfoResponse;
import com.betting.ground.deal.dto.response.SalesInfoResponse;
import com.betting.ground.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deal")
@Tag(name = "거래", description = "거래 내역 api")
public class DealController {
    private final DealService dealService;

    @GetMapping("/purchases")
    @Operation(summary = "구매 목록 조회")
    public Response<PurchaseInfoResponse> getPurchaseList(
            @Parameter(description = "filter 기능용 변수명 정해야함")
            @RequestParam(required = false) String status,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Pageable pageable
    ){
        return Response.success("구매 목록 조회 완료", new PurchaseInfoResponse());
    }

    @PutMapping("/{dealId}")
    @Operation(summary = "구매 확정")
    public Response<Void> purchaseComplete(
            @PathVariable Long dealId
    ){
        dealService.purchaseComplete(dealId);
        return Response.success("구매 확정 완료", null);
    }

    @GetMapping( "/bids")
    @Operation(summary = "경매 목록 조회")
    public Response<BiddingInfoResponse> getBiddingList(
            @Parameter(description = "filter 기능용 변수명 정해야함")
            @RequestParam(required = false) String status,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Pageable pageable
    ){
        return Response.success("경매 목록 조회 완료", new BiddingInfoResponse());
    }

    @GetMapping("/sales")
    @Operation(summary = "판매 목록 조회")
    public Response<SalesInfoResponse> getSalesList(
            @Parameter(description = "filter 기능용 변수명 정해야함")
            @RequestParam(required = false) String status,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Pageable pageable
    ){
        return Response.success("판매 목록 조회 완료", new SalesInfoResponse());
    }

    @GetMapping("/purchases/count")
    @Operation(summary = "구매 목록 게시물 수 조회")
    public Response<DealCountResponse> getPurchasesCount(){
        return Response.success("조회 완료", DealCountResponse.purchases(0, 0, 0));
    }

    @GetMapping("/bids/count")
    @Operation(summary = "경매 목록 게시물 수 조회")
    public Response<DealCountResponse> getBidsCount(){
        return Response.success("조회 완료", DealCountResponse.bids(0, 0, 0));
    }

    @GetMapping("/sales/count")
    @Operation(summary = "판매 목록 게시물 수 조회")
    public Response<DealCountResponse> getSalesCount(){
        return Response.success("조회 완료", DealCountResponse.sales(0, 0,0, 0));
    }
}
