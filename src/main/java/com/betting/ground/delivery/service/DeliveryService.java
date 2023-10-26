package com.betting.ground.delivery.service;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.delivery.domain.Delivery;
import com.betting.ground.delivery.dto.request.DeliveryInfoRequest;
import com.betting.ground.delivery.dto.response.DeliveryInfoResponse;
import com.betting.ground.delivery.repostiory.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final AuctionRepository auctionRepository;

    public DeliveryInfoResponse create(DeliveryInfoRequest request){
        Auction auction = auctionRepository.findById(request.getAuctionId()).orElseThrow(
                () -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND, "존재하지 않는 경매글입니다.")
        );

        Delivery delivery = new Delivery(request.getInvoice(), request.getDeliveryCompany(), auction);

        deliveryRepository.save(delivery);

        return new DeliveryInfoResponse(delivery);
    }
}
