package com.betting.ground.auction.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class biddingItemDto {
    @Schema(description = "경매글 아이디", example = "15")
    private Long auctionId;
    @Schema(description = "경매글 제목", example = "초이가 말아주는 버블티")
    private String title;
    @Schema(description = "경매글 사진 썸네일", example = "thumbnailImage-s3-url-15")
    private String thumbnailImage;
}
