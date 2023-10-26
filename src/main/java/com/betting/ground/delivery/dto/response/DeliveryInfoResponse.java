package com.betting.ground.delivery.dto.response;

import com.betting.ground.delivery.domain.Delivery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeliveryInfoResponse {

    @Schema(description = "배송 아이디", example = "1")
    private Long deliveryId;
    @Schema(description = "송장번호", example = "43252345546")
    private String invoice;
    @Schema(description = "택배회사", example = "우체국택배")
    private String deliveryCompany;

    public DeliveryInfoResponse(Delivery delivery) {
        this.deliveryId = delivery.getId();
        this.invoice = delivery.getInvoice();
        this.deliveryCompany = delivery.getDeliveryCompany();
    }
}
