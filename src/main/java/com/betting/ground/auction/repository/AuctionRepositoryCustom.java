package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuctionRepositoryCustom {

    PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByEndAuctionTimeAsc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable);

    BidInfoResponse getBidInfo(Long auctionId, Long userId);

    ItemDetailDto findDetailAuctionById(Long userId, Long auctionId);

    List<AuctionStatus> getAuctionByBidderId(Long userId);
}
