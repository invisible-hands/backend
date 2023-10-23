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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {

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
}
