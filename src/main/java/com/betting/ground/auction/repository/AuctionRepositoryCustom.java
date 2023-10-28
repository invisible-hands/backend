package com.betting.ground.auction.repository;

import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface AuctionRepositoryCustom {

    PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByEndAuctionTimeAsc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable);

    PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable);

    ItemDetailDto findDetailAuctionById(Long userId, Long auctionId);

    void findSellerByid(Long auctionId);
}
