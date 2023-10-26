package com.betting.ground.auction.repository;

import com.betting.ground.user.dto.response.PurchaseInfo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    PageImpl<PurchaseInfo> getAllPurchases(Long userId, Pageable pageable);

    PageImpl<PurchaseInfo> getBeforePurchases(long userId, Pageable pageable);
}
