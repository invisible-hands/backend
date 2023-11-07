package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.SellerItemDto;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuctionRepositoryCustom {

    PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByEndAuctionTimeAsc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable);

    ItemDetailDto findDetailAuctionById(LoginUser loginUser, Long auctionId);

    PageImpl<SellerItemDto> findSellerItemBySellerId(Long sellerId, Pageable pageable);

    Optional<User> findSellerById(Long auctionId);

    List<AuctionStatus> getAuctionByBidderId(Long userId);
}
