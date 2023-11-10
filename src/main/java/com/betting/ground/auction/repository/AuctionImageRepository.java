package com.betting.ground.auction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betting.ground.auction.domain.AuctionImage;

public interface AuctionImageRepository extends JpaRepository<AuctionImage, Long>, AuctionImageRepositoryCustom {
	Optional<AuctionImage> findFirstByAuctionId(Long auctionId);

	List<AuctionImage> findByAuctionIdIn(List<Long> auctionIds);
}
