package com.betting.ground.auction.controller;

import com.betting.ground.auction.domain.dto.BidHistoryDto;
import com.betting.ground.auction.domain.dto.BidInfo;
import com.betting.ground.auction.domain.dto.ItemDetailDto;
import com.betting.ground.auction.domain.request.AuctionCreateRequest;
import com.betting.ground.auction.domain.request.BidRequest;
import com.betting.ground.auction.domain.response.SearchResponse;
import com.betting.ground.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import com.betting.ground.auction.dto.ItemResponse;
import com.betting.ground.auction.service.AuctionService;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
@Tag(name = "경매", description = "경매 관련 api")
public class AuctionController {
    private final AuctionService auctionService;
  
    @GetMapping("/{auctionId}")
    @Operation(summary = "경매 상세 정보", description = "")
    @Parameter(name = "auctionId", description = "경매글 아이디", example = "1")
    public Response<ItemDetailDto> getItemDetail(@PathVariable Long auctionId) {
        return Response.success("해당 경매글 보기 성공", new ItemDetailDto());
    }

    @GetMapping("/{auctionId}/bidHistory")
    @Operation(summary = "입찰 내역", description = "")
    @Parameter(name = "auctionId", description = "경매글 아이디", example = "4")
    @Parameter(name = "pageable", description = "page 와 size 보내주세요")
    public Response<BidHistoryDto> getBidHistory(@PathVariable Long auctionId, Pageable pageable) {
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

    @Operation(summary = "게시글 검색")
    @GetMapping("/search")
    public Response<SearchResponse> search(
            @RequestParam String keyword,
            @Parameter(description = "page랑 size만 주시면 됩니다. ")
            Pageable pageable){
        return Response.success("게시글 조회 성공", SearchResponse.builder().build());
    }

    @Operation(summary = "경매 생성", description = "request는 json으로 보내주셔야 합니다!")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Void> create(
            @Parameter(description = "request는 json으로 보내주셔야 합니다!")
            @RequestPart AuctionCreateRequest request,
            @RequestParam List<MultipartFile> images
    ) {
        return Response.success("게시글 생성 성공", null);
    }

    @Operation(summary = "경매 삭제")
    @DeleteMapping("/{auctionId}")
    public Response<Void> delete(@PathVariable Long auctionId){
        return Response.success("게시글 삭제 성공", null);
    }

    @Operation(summary = "입찰 요청")
    @PostMapping("/{auctionId}/bid")
    public Response<Void> bid(@PathVariable Long auctionId, @RequestBody BidRequest request){
        return Response.success("경매 참여 완료", null);
    }

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
