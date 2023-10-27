package com.betting.ground.deal.service;

import com.betting.ground.admin.domain.Settlement;
import com.betting.ground.admin.repository.SettlementRepository;
import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.BidHistory;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.auction.repository.BidHistoryRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.dto.response.BiddingInfoResponse;
import com.betting.ground.deal.dto.response.DealCountResponse;
import com.betting.ground.deal.dto.response.PurchaseInfoResponse;
import com.betting.ground.deal.dto.response.SalesInfoResponse;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

    private final DealRepository dealRepository;
    private final AuctionRepository auctionRepository;
    private final DealEventRepository dealEventRepository;
    private final SettlementRepository settlementRepository;

    @Transactional(readOnly = true)
    public PurchaseInfoResponse getAllPurchases(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate){
        return new PurchaseInfoResponse(dealRepository.getAllPurchases(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public PurchaseInfoResponse getProgressPurchases(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new PurchaseInfoResponse(dealRepository.getProgressPurchases(userId, pageable, startDate, endDate));
    }

    public void purchaseComplete(Long dealId) {
        Deal deal = dealRepository.findById(dealId).orElseThrow(
                () -> new GlobalException(ErrorCode.DEAL_NOT_FOUND)
        );

        deal.updateStatus(DealStatus.PURCHASE_COMPLETE);
        dealEventRepository.save(new DealEvent(deal));
        settlementRepository.save(new Settlement(deal));
    }

    @Transactional(readOnly = true)
    public PurchaseInfoResponse getCompletePurchases(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new PurchaseInfoResponse(dealRepository.getCompletePurchases(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public BiddingInfoResponse getAllBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new BiddingInfoResponse(dealRepository.getAllBidding(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public BiddingInfoResponse getProgressBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new BiddingInfoResponse(dealRepository.getProgressBidding(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public BiddingInfoResponse getCompleteBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new BiddingInfoResponse(dealRepository.getCompleteBidding(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public SalesInfoResponse getAllSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new SalesInfoResponse(dealRepository.getAllSales(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public SalesInfoResponse getBeforeSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new SalesInfoResponse(dealRepository.getBeforeSales(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public SalesInfoResponse getProgressSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new SalesInfoResponse(dealRepository.getProgressSales(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public SalesInfoResponse getCompleteSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        return new SalesInfoResponse(dealRepository.getCompleteSales(userId, pageable, startDate, endDate));
    }

    @Transactional(readOnly = true)
    public DealCountResponse getPurchasesCount(Long userId){
        int progress = 0;
        int complete = 0;

        List<Deal> list = dealRepository.findAllByBuyerId(userId);
        for (Deal deal : list) {
            if(deal.getDealStatus().equals(DealStatus.PURCHASE_COMPLETE) || deal.getDealStatus().equals(DealStatus.PURCHASE_COMPLETE))
                complete++;
            else
                progress++;
        }

        return DealCountResponse.purchases(progress + complete, progress, complete);
    }

    @Transactional(readOnly = true)
    public DealCountResponse getBiddingCount(Long userId){
        int progress = 0;
        int complete = 0;

        List<AuctionStatus> list = auctionRepository.getAuctionByBidderId(userId);

        for (AuctionStatus status : list) {
            if (status.equals(AuctionStatus.AUCTION_PROGRESS))
                progress++;
            else
                complete++;
        }

        return DealCountResponse.bids(progress+complete, progress, complete);
    }

    @Transactional(readOnly = true)
    public DealCountResponse getSalesCount(Long userId) {
        int before = 0;
        int progress = 0;
        int complete = 0;

        List<Deal> sales = dealRepository.findAllBySellerId(userId);

        for (Deal deal : sales) {
            if(deal.getDealStatus().equals(DealStatus.DELIVERY_WAITING))
                before++;
            else if(deal.getDealStatus().equals(DealStatus.PURCHASE_COMPLETE_WAITING))
                progress++;
            else
                complete++;
        }

        return DealCountResponse.sales(before+progress+complete, before, progress, complete);
    }
}
