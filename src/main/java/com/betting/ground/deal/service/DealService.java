package com.betting.ground.deal.service;

import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

    private final AuctionRepository auctionRepository;
    private final DealRepository dealRepository;

//    public PurchaseInfoResponse getAllPurchases(Long userId, Pageable pageable){
//        return new PurchaseInfoResponse(dealRepository.getAllPurchases(userId, pageable));
//    }
//
//    public PurchaseInfoResponse getBeforeShippingPurchases(Long userId, Pageable pageable) {
//        return new PurchaseInfoResponse(dealRepository.getBeforePurchases(userId, pageable));
//    }

    public void purchaseComplete(Long dealId) {
        Deal deal = dealRepository.findById(dealId).orElseThrow(
                () -> new GlobalException(ErrorCode.DEAL_NOT_FOUND)
        );

        deal.updateStatus(DealStatus.PURCHASE_COMPLETE);
    }
}
