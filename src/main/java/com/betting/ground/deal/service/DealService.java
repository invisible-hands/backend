package com.betting.ground.deal.service;

import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.dto.response.BiddingInfoResponse;
import com.betting.ground.deal.dto.response.PurchaseInfoResponse;
import com.betting.ground.deal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

    private final AuctionRepository auctionRepository;
    private final DealRepository dealRepository;

    public PurchaseInfoResponse getAllPurchases(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate){
        return new PurchaseInfoResponse(dealRepository.getAllPurchases(userId, pageable, startDate, endDate));
    }

    public PurchaseInfoResponse getProgressPurchases(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new PurchaseInfoResponse(dealRepository.getProgressPurchases(userId, pageable, startDate, endDate));
    }

    public void purchaseComplete(Long dealId) {
        Deal deal = dealRepository.findById(dealId).orElseThrow(
                () -> new GlobalException(ErrorCode.DEAL_NOT_FOUND)
        );

        deal.updateStatus(DealStatus.PURCHASE_COMPLETE);
    }

    public PurchaseInfoResponse getCompletePurchases(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new PurchaseInfoResponse(dealRepository.getCompletePurchases(userId, pageable, startDate, endDate));
    }

    public BiddingInfoResponse getAllBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new BiddingInfoResponse(dealRepository.getAllBidding(userId, pageable, startDate, endDate));
    }

    public BiddingInfoResponse getProgressBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new BiddingInfoResponse();
    }

    public BiddingInfoResponse getCompleteBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new BiddingInfoResponse();
    }
}
