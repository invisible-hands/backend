package com.betting.ground.deal.repository;

import com.betting.ground.deal.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DealRepository extends JpaRepository<Deal, Long>, DealRepositoryCustom {
    Optional<Deal> findByAuctionId(Long auctionId);

    List<Deal> findAllByBuyerId(Long userId);

    List<Deal> findAllBySellerId(Long userId);
}
