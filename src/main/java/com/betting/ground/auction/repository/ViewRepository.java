package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.View;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewRepository extends JpaRepository<View, Long> {

    List<View> findAllByAuctionIdIn(List<Long> auctions);
}