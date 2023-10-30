package com.betting.ground.auction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BidInfo {
    @Schema(description = "입찰 아이디", example="4")
    private Long bidId;
    @Schema(description = "입찰자 아이디", example="14")
    private Long bidderId;
    @Schema(description = "입찰자 닉네임", example="고집쎈오리너구리")
    private String bidderNickname;
    @Schema(description = "입찰 시간", example="2023-10-20 13:35:10")
    private String bidTime;
    @Schema(description = "입찰 가격", example="185000")
    private Long bidPrice;

    @Builder
    public BidInfo(Long bidId, Long bidderId, String bidderNickname, LocalDateTime bidTime, Long bidPrice) {
        this.bidId = bidId;
        this.bidderId = bidderId;
        this.bidderNickname = bidderNickname;
        this.bidTime = bidTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
        this.bidPrice = bidPrice;
    }
}
