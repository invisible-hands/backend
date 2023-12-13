package com.betting.ground.auction.dto;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.Duration;
import com.betting.ground.auction.domain.ItemCondition;
import com.betting.ground.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter @Setter
@Builder
public class CreateAuctionDto {
    private String title;
    private String content;
    private ItemCondition itemCondition;
    private Long startPrice;
    private Long instantPrice;
    private Duration duration;
    private List<MultipartFile> images;
    private List<String> tags;

    public Auction toEntity(User user) {
        return Auction.builder()
                .user(user)
                .title(this.title)
                .content(this.content)
                .itemCondition(this.itemCondition)
                .startPrice(this.startPrice)
                .instantPrice(this.instantPrice)
                .duration(this.duration)
                .build();
    }
}
