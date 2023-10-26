package com.betting.ground.auction.dto.response;

import com.betting.ground.auction.dto.AuctionDetailInfo;
import com.betting.ground.auction.dto.AuctionImageDto;
import com.betting.ground.auction.dto.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemDetailDto {
    @Schema(description = "경매 상세 정보")
    private AuctionDetailInfo auctionInfo;
    @Schema(description = "상품 이미지", example="")
    private List<AuctionImageDto> images;
    @Schema(description = "태그", example="")
    private List<TagDto> tags;
    @Schema(description = "본인 경매글인지 여부", example= "false")
    private boolean authorCheck;
}
