package com.betting.ground.auction.controller;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.dto.biddingItemDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class SellerInfo {

    @Schema(description = "판매자 아이디", example="6")
    private Long sellerId;
    @Schema(description = "판매자 닉네임", example="biggu")
    private String nickname;
    @Schema(description = "판매자 경매글 수", example="5")
    private int auctionCnt;
    @Schema(description = "판매자 경매글 목록")
    private List<biddingItemDto> auctionList;
    @Schema(description = "현재 페이지", example="1")
    private int currentPage;
    @Schema(description = "전체 페이지", example="2")
    private int totalPage;

}
