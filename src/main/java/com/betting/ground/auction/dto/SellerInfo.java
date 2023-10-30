package com.betting.ground.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SellerInfo {

    @Schema(description = "판매자 아이디", example="6")
    private Long sellerId;
    @Schema(description = "판매자 닉네임", example="biggu")
    private String nickname;
    @Schema(description = "판매자 프로필 이미지", example="profileImage-s3-url")
    private String profileImage;
    @Schema(description = "판매자 경매글 수", example="5")
    private Long auctionCnt;
    @Schema(description = "판매자 경매글 목록 3개만 보여줌")
    private List<BiddingItemDto> auctionList;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "전체 페이지", example="3")
    private int totalPage;

    @Builder
    public SellerInfo(Long sellerId, String nickname, String profileImage, Long auctionCnt, List<BiddingItemDto> auctionList, int currentPage, int totalPage) {
        this.sellerId = sellerId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.auctionCnt = auctionCnt;
        this.auctionList = auctionList;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }
}
