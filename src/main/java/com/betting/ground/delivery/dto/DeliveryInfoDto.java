package com.betting.ground.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryInfoDto {
    @Schema(description = "옥션 Id", example = "1")
    private Long auctionId;
    @Schema(description = "송장번호", example = "43252345546")
    private String invoice;
    @Schema(description = "택배회사", example = "우체국택배")
    private String deliveryCompany;
}
