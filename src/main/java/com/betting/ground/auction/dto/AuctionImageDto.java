package com.betting.ground.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuctionImageDto {
    @Schema(description = "상품 이미지 id", example="14")
    private Long imageId;
    @Schema(description = "상품 이미지 url", example="aws-s3-url-14")
    private String imageUrl;

}
