package com.betting.ground.auction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;

import jakarta.persistence.LockModeType;

public interface AuctionRepository extends JpaRepository<Auction, Long>, AuctionRepositoryCustom {
	List<Auction> findAllByAuctionStatus(AuctionStatus auctionProgress);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from Auction a where a.id=:auctionId")
	Optional<Auction> findByLockId(Long auctionId);

	List<Auction> findByIdIn(List<Long> ids);

	List<Auction> findByIdInAndAuctionStatus(List<Long> list, AuctionStatus auctionStatus);
}
