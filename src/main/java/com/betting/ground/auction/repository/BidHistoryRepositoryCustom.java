package com.betting.ground.auction.repository;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.betting.ground.auction.dto.BidInfo;

public interface BidHistoryRepositoryCustom {
	PageImpl<BidInfo> findBidInfoByAuctionId(Long auctionId, Pageable pageable);
}
