package com.betting.ground.auction.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SellerInfo {

    @Schema(description = "판매자 아이디", example="6")
    private Long sellerId;
    @Schema(description = "판매자 닉네임", example="biggu")
    private String nickname;
    @Schema(description = "판매자 프로필 이미지", example="profileImage-s3-url")
    private String profileImage;
    @Schema(description = "판매자 경매글 수", example="5")
    private int auctionCnt;
    @Schema(description = "판매자 경매글 목록 3개만 보여줌")
    private List<biddingItemDto> auctionList;

    @QueryProjection
    public SellerInfo(Long sellerId, String nickname, String profileImage, int auctionCnt, List<biddingItemDto> auctionList) {
        this.sellerId = sellerId;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.auctionCnt = auctionCnt;
        this.auctionList = auctionList;
    }
}
