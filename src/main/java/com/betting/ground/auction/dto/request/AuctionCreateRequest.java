package com.betting.ground.auction.dto.request;

import com.betting.ground.auction.domain.Duration;
import com.betting.ground.auction.domain.ItemCondition;
import com.betting.ground.auction.dto.CreateAuctionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuctionCreateRequest {
    @Schema(description = "물품 제목", example = "내가 만든 쿠키")
    @NotNull
    private String title;
    @Schema(description = "물품 설명", example = "뉴진스가 아니라 제가 구운 쿠키 전혀 건강을 생각하지 않아 버터를 때려박았어요")
    @NotNull
    private String content;
    @Schema(description = "새제품/중고제품 구분", example = "NEW")
    @NotNull
    private String itemCondition;
    @Schema(description = "경매 시작가", example = "10000")
    @NotNull
    private Long startPrice;
    @Schema(description = "즉시 판매가", example = "25000")
    @NotNull
    private Long instantPrice;
    @Schema(description = "경매 시간", example = "DAY", allowableValues = {"QUARTER", "HALF", "DAY"})
    @NotNull
    private String duration;
    @Schema(description = "태그", example = "")
    private List<String> tags;

    public CreateAuctionDto toDto(List<MultipartFile> images) {
        return CreateAuctionDto.builder()
                .title(this.title)
                .content(this.content)
                .itemCondition(ItemCondition.valueOf(this.itemCondition))
                .startPrice(this.startPrice)
                .instantPrice(this.instantPrice)
                .duration(Duration.valueOf(this.duration))
                .images(images)
                .tags(this.tags)
                .build();
    }
}
