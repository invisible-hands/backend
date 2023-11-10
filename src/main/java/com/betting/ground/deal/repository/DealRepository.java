package com.betting.ground.deal.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealStatus;

public interface DealRepository extends JpaRepository<Deal, Long> {
	Optional<Deal> findByAuctionId(Long auctionId);

	List<Deal> findAllByBuyerId(Long userId);

	List<Deal> findAllBySellerId(Long userId);

	List<Deal> findAllByDealStatus(DealStatus deliveryWaiting);

	List<Deal> findByBuyerIdAndDealStatusInAndDealTimeBetweenOrderByDealTimeDesc(Long userId, Pageable pageable,
		List<DealStatus> status,
		LocalDateTime startDate, LocalDateTime endDate);

	List<Deal> findBySellerIdAndDealStatusInAndDealTimeBetweenOrderByDealTimeDesc(Long userId, Pageable pageable,
		List<DealStatus> status,
		LocalDateTime startDate, LocalDateTime endDate);

	List<Deal> findByBuyerIdAndDealStatusInAndDealTimeBetween(Long userId, List<DealStatus> status,
		LocalDateTime localDateTime, LocalDateTime localDateTime1);

	List<Deal> findBySellerIdAndDealStatusInAndDealTimeBetween(Long userId, List<DealStatus> status,
		LocalDateTime startDate, LocalDateTime endDate);
}
