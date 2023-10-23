package com.betting.ground.auction.controller;

import com.betting.ground.auction.domain.request.AuctionCreateRequest;
import com.betting.ground.auction.domain.response.SearchResponse;
import com.betting.ground.common.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction")
public class AuctionController {

    @GetMapping
    public Response<SearchResponse> search(@RequestParam String title){
        return Response.success("게시글 조회 성공", SearchResponse.builder().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Void> create(
            @RequestPart AuctionCreateRequest request,
            @RequestParam List<MultipartFile> images
    ) {
        return Response.success("게시글 생성 성공", null);
    }

    @DeleteMapping("/{auctionId}")
    public Response<Void> delete(@PathVariable Long auctionId){
        return Response.success("게시글 삭제 성공", null);
    }

    @PostMapping("/bid/{auctionId}")
    public Response<Void> bid(@PathVariable Long auctionId){
        return Response.success("경매 참여 완료", null);
    }
}
