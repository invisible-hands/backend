package com.betting.ground.auction.controller;

import static com.betting.ground.common.exception.ErrorCode.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.betting.ground.auction.dto.BidHistoryDto;
import com.betting.ground.auction.dto.CreateAuctionDto;
import com.betting.ground.auction.dto.SellerInfo;
import com.betting.ground.auction.dto.request.AuctionCreateRequest;
import com.betting.ground.auction.dto.request.PayRequest;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.auction.dto.response.ItemResponse;
import com.betting.ground.auction.service.AuctionService;
import com.betting.ground.common.dto.Response;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.dto.login.LoginUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
@Tag(name = "경매", description = "경매 관련 api")
public class AuctionController {

	private final AuctionService auctionService;

	@GetMapping("/{auctionId}")
	@Operation(summary = "경매 상세 정보", description = "")
	public Response<ItemDetailDto> getItemDetail(
		@Parameter(description = "경매글 아이디", example = "1")
		@PathVariable Long auctionId,
		@AuthenticationPrincipal LoginUser loginUser,
		HttpServletRequest request,
		HttpServletResponse response
	) {
		String uuid = UUID.randomUUID().toString();

		ResponseCookie responseCookie = ResponseCookie.from("UserUUID", uuid)
			.maxAge(24 * 60 * 60)
			.httpOnly(false)
			.sameSite("None")
			.secure(true)
			.path("/")
			.build();

		if (request.getCookies() == null) {
			response.setHeader("Set-Cookie", responseCookie.toString());
		} else {
			uuid = Arrays.stream(request.getCookies())
				.filter(c -> c.getName().equals("UserUUID"))
				.map(c -> c.getValue())
				.findFirst().orElse(uuid);
		}

		return Response.success("해당 경매글 보기 성공", auctionService.getItemDetail(loginUser, auctionId, uuid));
	}

	@GetMapping("/{auctionId}/bidHistory")
	@Operation(summary = "입찰 내역", description = "")
	public Response<BidHistoryDto> getBidHistory(
		@Parameter(description = "경매글 아이디", example = "4")
		@PathVariable Long auctionId,
		@Parameter(description = "page 와 size 보내주세요")
		Pageable pageable) {

		return Response.success("해당 경매글의 입찰 내역 보기 성공", auctionService.getBidHistory(auctionId, pageable));
	}

	@GetMapping("/{auctionId}/seller")
	@Operation(summary = "판매자 정보", description = "")
	public Response<SellerInfo> getSeller(
		@Parameter(name = "auctionId", description = "경매글 아이디", example = "4")
		@PathVariable Long auctionId,
		@Parameter(description = "page 와 size 보내주세요")
		Pageable pageable) {

		return Response.success("해당 경매글의 판매자 정보 보기 성공", auctionService.getSeller(auctionId, pageable));
	}

	@Operation(summary = "게시글 검색")
	@GetMapping("/search")
	public Response<ItemResponse> search(
		@RequestParam String keyword,
		@Parameter(description = "page랑 size만 주시면 됩니다. ")
		Pageable pageable
	) {
		return Response.success("게시글 조회 성공", auctionService.search(keyword, pageable));
	}

	@Operation(summary = "경매 생성", description = "request는 json으로 보내주셔야 합니다!")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Response<Void> create(
		@AuthenticationPrincipal LoginUser loginUser,
		@Parameter(description = "request는 json으로 보내주셔야 합니다!")
		@RequestPart AuctionCreateRequest request,
		@RequestPart(required = false) List<MultipartFile> images
	) throws IOException {

		if (request.getInstantPrice() < request.getStartPrice()) {
			throw new GlobalException(INSTANT_PRICE_LESS_THAN_START_PRICE);
		}

		CreateAuctionDto createAuctionDto = request.toDto(images);
		auctionService.create(loginUser.getUser().getId(), createAuctionDto);

		return Response.success("게시글 생성 성공", null);
	}

	@Operation(summary = "경매 삭제")
	@DeleteMapping("/{auctionId}")
	public Response<Void> delete(@AuthenticationPrincipal LoginUser loginUser, @PathVariable Long auctionId) {
		auctionService.delete(loginUser.getUser().getId(), auctionId);

		return Response.success("게시글 삭제 성공", null);
	}

	@Operation(summary = "입찰 요청")
	@PostMapping("/{auctionId}/bid")
	public Response<Void> bid(@PathVariable Long auctionId,
		@RequestBody PayRequest request,
		@AuthenticationPrincipal LoginUser loginUser) {
		auctionService.bid(auctionId, request, loginUser.getUser().getId());
		return Response.success("경매 참여 완료", null);
	}

	@PostMapping("/{auctionId}/instant")
	@Operation(summary = "즉시 결제", description = "")
	public Response<Void> instantBuy(
		@Parameter(name = "auctionId", description = "경매글 아이디", example = "4")
		@PathVariable Long auctionId,
		@AuthenticationPrincipal LoginUser loginUser
	) {
		auctionService.instantBuy(auctionId, loginUser.getUser().getId());
		return Response.success("해당 경매글의 즉시 결제 성공", null);
	}

	@Operation(summary = "경매 목록(최신순, 최근 마감일순, 조회수순")
	@GetMapping
	public Response<ItemResponse> getNewItem(
		@Parameter(
			description = "ex) ?page=1&size=10&sort=latest,desc&progressFilter=true"
		) Pageable pageable,
		@RequestParam Boolean progressFilter) {
		return Response.success("경매 목록", auctionService.getItems(pageable, progressFilter));
	}

	@GetMapping("/{auctionId}/bid")
	@Operation(summary = "상품 입찰하기 페이지 조회")
	public Response<BidInfoResponse> getBidInfo(@PathVariable Long auctionId,
		@AuthenticationPrincipal LoginUser loginUser) {
		return Response.success("입찰 조회 완료", auctionService.getBidInfo(auctionId, loginUser.getUser().getId()));
	}
}
