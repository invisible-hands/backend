package com.betting.ground.auction.repository;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.SellerItemDto;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.user.dto.login.LoginUser;

public interface AuctionRepositoryCustom {

	PageImpl<AuctionInfo> findItems(Pageable pageable, Boolean progressFilter);

	PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable);

	ItemDetailDto findDetailAuctionById(LoginUser loginUser, Long auctionId);

	PageImpl<SellerItemDto> findSellerItemBySellerId(Long sellerId, Pageable pageable);

	List<AuctionStatus> getAuctionByBidderId(Long userId);
}
