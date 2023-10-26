package com.betting.ground.auction.repository;

import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositoryCustom {

    PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByEndAuctionTimeAsc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable);

    BidInfoResponse getBidInfo(Long auctionId, Long userId);
}
