package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long>{
}
