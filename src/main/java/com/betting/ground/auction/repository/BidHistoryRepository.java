package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long>, BidHistoryRepositoryCustom {
    Optional<BidHistory> findByAuctionAndPrice(Auction auction, Long currentPrice);
}
