package com.betting.ground.delivery.service;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.repository.DealRepository;
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
    private final DealRepository dealRepository;

    public DeliveryInfoResponse create(DeliveryInfoRequest request){
        Auction auction = auctionRepository.findById(request.getAuctionId()).orElseThrow(
                () -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND, "존재하지 않는 경매글입니다.")
        );
        Deal deal = dealRepository.findByAuctionId(auction.getId()).orElseThrow(
                () -> new GlobalException(ErrorCode.DEAL_NOT_FOUND)
        );

        deal.updateStatus(DealStatus.PURCHASE_COMPLETE_WAITING);

        Delivery delivery = new Delivery(request.getInvoice(), request.getDeliveryCompany(), auction);

        deliveryRepository.save(delivery);

        return new DeliveryInfoResponse(delivery);
    }
}
