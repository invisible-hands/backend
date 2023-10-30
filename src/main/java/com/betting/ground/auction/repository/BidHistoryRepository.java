package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.BidHistory;
import com.betting.ground.auction.dto.BidInfo;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long>, BidHistoryRepositoryCustom {
}
