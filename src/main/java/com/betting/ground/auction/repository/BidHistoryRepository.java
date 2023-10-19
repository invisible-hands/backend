package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long> {
}
