package com.betting.ground.auction.dto;

import com.betting.ground.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

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
    private List<SellerItemDto> auctionList;
    @Schema(description = "현재 페이지", example="0")
    private int currentPage;
    @Schema(description = "전체 페이지", example="3")
    private int totalPage;

    public SellerInfo(User seller, PageImpl<SellerItemDto> sellerItem) {
        this.sellerId = seller.getId();
        this.nickname = seller.getNickname();
        this.profileImage = seller.getProfileImage();
        this.auctionCnt = sellerItem.getTotalElements();
        this.auctionList = sellerItem.getContent();
        this.currentPage = sellerItem.getNumber();
        this.totalPage = sellerItem.getTotalPages();
    }
}
