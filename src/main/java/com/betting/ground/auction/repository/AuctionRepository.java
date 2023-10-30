package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionRepositoryCustom {
    List<Auction> findAllByAuctionStatus(AuctionStatus auctionProgress);
}
