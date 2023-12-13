package com.betting.ground.deal.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betting.ground.common.dto.Response;
import com.betting.ground.deal.dto.response.BiddingInfoResponse;
import com.betting.ground.deal.dto.response.DealCountResponse;
import com.betting.ground.deal.dto.response.PurchaseInfoResponse;
import com.betting.ground.deal.dto.response.SalesInfoResponse;
import com.betting.ground.deal.service.DealService;
import com.betting.ground.user.dto.login.LoginUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deal")
@Tag(name = "거래", description = "거래 내역 api")
public class DealController {
	private final DealService dealService;

	@GetMapping
	public String test(String test) {
		return test;
	}

	@GetMapping("/purchases")
	@Operation(summary = "구매 목록 조회")
	public Response<PurchaseInfoResponse> getPurchaseList(
		@Parameter(description = "all/progress/waiting/complete")
		@RequestParam(defaultValue = "all") String status,
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
		Pageable pageable,
		@AuthenticationPrincipal LoginUser loginUser
	) {
		PurchaseInfoResponse response;

		if (status.equals("all"))
			response = dealService.getAllPurchases(loginUser.getUser().getId(), pageable, startDate, endDate);
		else if (status.equals("progress"))
			response = dealService.getProgressPurchases(loginUser.getUser().getId(), pageable, startDate, endDate);
		else if (status.equals("waiting"))
			response = dealService.getWaitingPurchases(loginUser.getUser().getId(), pageable, startDate, endDate);
		else
			response = dealService.getCompletePurchases(loginUser.getUser().getId(), pageable, startDate, endDate);

		return Response.success("구매 목록 조회 완료", response);
	}

	@PutMapping("/{dealId}")
	@Operation(summary = "구매 확정")
	public Response<Void> purchaseComplete(
		@PathVariable Long dealId
	) {
		dealService.purchaseComplete(dealId);
		return Response.success("구매 확정 완료", null);
	}

	@GetMapping("/bids")
	@Operation(summary = "경매 목록 조회")
	public Response<BiddingInfoResponse> getBiddingList(
		@Parameter(description = "all/progress/complete")
		@RequestParam(defaultValue = "all") String status,
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
		Pageable pageable,
		@AuthenticationPrincipal LoginUser user
	) {
		BiddingInfoResponse response;

		if (status.equals("all"))
			response = dealService.getAllBidding(user.getUser().getId(), pageable, startDate, endDate);
		else if (status.equals("progress"))
			response = dealService.getProgressBidding(user.getUser().getId(), pageable, startDate, endDate);
		else
			response = dealService.getCompleteBidding(user.getUser().getId(), pageable, startDate, endDate);

		return Response.success("경매 목록 조회 완료", response);
	}

	@GetMapping("/sales")
	@Operation(summary = "판매 목록 조회")
	public Response<SalesInfoResponse> getSalesList(
		@Parameter(description = "all/before/progress/complete")
		@RequestParam(defaultValue = "all") String status,
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
		@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
		Pageable pageable,
		@AuthenticationPrincipal LoginUser user
	) {
		SalesInfoResponse response;

		if (status.equals("all"))
			response = dealService.getAllSales(user.getUser().getId(), pageable, startDate, endDate);
		else if (status.equals("before"))
			response = dealService.getBeforeSales(user.getUser().getId(), pageable, startDate, endDate);
		else if (status.equals("progress"))
			response = dealService.getProgressSales(user.getUser().getId(), pageable, startDate, endDate);
		else
			response = dealService.getCompleteSales(user.getUser().getId(), pageable, startDate, endDate);

		return Response.success("판매 목록 조회 완료", response);
	}

	@GetMapping("/purchases/count")
	@Operation(summary = "구매 목록 게시물 수 조회")
	public Response<DealCountResponse> getPurchasesCount(@AuthenticationPrincipal LoginUser loginUser) {
		return Response.success("조회 완료", dealService.getPurchasesCount(loginUser.getUser().getId()));
	}

	@GetMapping("/bids/count")
	@Operation(summary = "경매 목록 게시물 수 조회")
	public Response<DealCountResponse> getBidsCount(@AuthenticationPrincipal LoginUser loginUser) {
		return Response.success("조회 완료", dealService.getBiddingCount(loginUser.getUser().getId()));
	}

	@GetMapping("/sales/count")
	@Operation(summary = "판매 목록 게시물 수 조회")
	public Response<DealCountResponse> getSalesCount(@AuthenticationPrincipal LoginUser loginUser) {
		return Response.success("조회 완료", dealService.getSalesCount(loginUser.getUser().getId()));
	}
}
