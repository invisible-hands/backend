package com.betting.ground.auction.repository;

import com.betting.ground.auction.dto.BidInfo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface BidHistoryRepositoryCustom {
    PageImpl<BidInfo> findBidInfoByAuctionId(Long auctionId, Pageable pageable);
}
