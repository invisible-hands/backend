package com.betting.ground.auction.repository;

import com.betting.ground.auction.dto.BiddingItemDto;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositoryCustom {

    PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByEndAuctionTimeAsc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable);

    BidInfoResponse getBidInfo(Long auctionId, Long userId);
  
    ItemDetailDto findDetailAuctionById(LoginUser loginUser, Long auctionId);

    PageImpl<BiddingItemDto> findSellerItemBySellerId(Long sellerId, Pageable pageable);

    User findSellerById(Long auctionId);
}
