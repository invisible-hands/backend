package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.AuctionImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionImageRepository extends JpaRepository<AuctionImage, Long>, AuctionImageRepositoryCustom {
    Optional<AuctionImage> findFirstByAuctionId(Long auctionId);
}
