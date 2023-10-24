package com.betting.ground.auction.dto;

import com.betting.ground.auction.domain.AuctionImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class AuctionImageDto {
    @Schema(description = "상품 이미지 id", example="14")
    private Long imageId;
    @Schema(description = "상품 이미지 url", example="aws-s3-url-14")
    private String imageUrl;

    public AuctionImageDto(AuctionImage auctionImage) {
        this.imageId = auctionImage.getId();
        this.imageUrl = auctionImage.getImageUrl();
    }
}
