package com.betting.ground.delivery.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.delivery.dto.DeliveryInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/delivery")
@Tag(name = "배송", description = "배송 api")
public class DeliveryController {

    @PostMapping
    @Operation(summary = "송장 번호 등록")
    public Response<DeliveryInfoDto> purchaseComplete(
            @RequestBody DeliveryInfoDto request
    ){
        return Response.success("송장 번호 등록", new DeliveryInfoDto());
    }
}
