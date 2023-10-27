package com.betting.ground.deal.repository;

import com.betting.ground.deal.dto.response.BiddingInfo;
import com.betting.ground.deal.dto.response.PurchaseInfo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DealRepositoryCustom {
    PageImpl<PurchaseInfo> getAllPurchases(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate);

    PageImpl<PurchaseInfo> getProgressPurchases(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate);

    PageImpl<PurchaseInfo> getCompletePurchases(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate);

    PageImpl<BiddingInfo> getAllBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate);
}
