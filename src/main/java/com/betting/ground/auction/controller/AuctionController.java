package com.betting.ground.auction.controller;

import com.betting.ground.auction.domain.request.AuctionCreateRequest;
import com.betting.ground.auction.domain.request.BidRequest;
import com.betting.ground.auction.domain.response.SearchResponse;
import com.betting.ground.common.dto.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    @Operation(summary = "게시글 검색")
    @GetMapping("/search")
    public Response<SearchResponse> search(
            @RequestParam String keyword,
            @Parameter(description = "page랑 size만 주시면 됩니다. ")
            Pageable pageable){
        return Response.success("게시글 조회 성공", SearchResponse.builder().build());
    }

    @Operation(summary = "경매 생성")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Void> create(
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
