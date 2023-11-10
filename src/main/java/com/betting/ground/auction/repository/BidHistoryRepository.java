package com.betting.ground.auction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.BidHistory;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long>, BidHistoryRepositoryCustom {
	Optional<BidHistory> findByAuctionAndPrice(Auction auction, Long currentPrice);

	List<BidHistory> findByBidderIdAndCreatedAtBetweenOrderByCreatedAtDesc(long userId, Pageable pageable,
		LocalDateTime startDate,
		LocalDateTime endDate);

	List<BidHistory> findByBidderIdAndCreatedAtBetweenOrderByCreatedAtDesc(long userId, LocalDateTime localDateTime,
		LocalDateTime localDateTime1);

	List<BidHistory> findByBidderIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);
}
