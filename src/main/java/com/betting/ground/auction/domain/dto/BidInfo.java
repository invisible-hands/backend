package com.betting.ground.auction.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BidInfo {
    @Schema(description = "입찰 아이디", example="4")
    private Long bidId;
    @Schema(description = "입찰자 아이디", example="14")
    private Long bidderId;
    @Schema(description = "입찰자 이메일", example="harok@naver.com")
    private String bidderEmail;
    @Schema(description = "입찰 시간", example="2023-10-20 13:35:10")
    private String bidTime;
    @Schema(description = "입찰 가격", example="185000")
    private Long bidPrice;
}
