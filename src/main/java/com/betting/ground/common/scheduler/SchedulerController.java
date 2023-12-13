package com.betting.ground.common.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.View;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.auction.repository.ViewCacheRepository;
import com.betting.ground.auction.repository.ViewRepository;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import com.betting.ground.user.domain.Payment;
import com.betting.ground.user.domain.PaymentType;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.repository.PaymentRepository;
import com.betting.ground.user.repository.UserRepository;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Slf4j
@Hidden
public class SchedulerController {
	private final AuctionRepository auctionRepository;
	private final DealRepository dealRepository;
	private final DealEventRepository dealEventRepository;
	private final ViewRepository viewRepository;
	private final ViewCacheRepository viewCacheRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@GetMapping("/auction")
	@Transactional
	public void update() {
		log.info("status update");
		List<Auction> auctions = auctionRepository.findAllByAuctionStatus(AuctionStatus.AUCTION_PROGRESS);
		List<DealEvent> dealEvents = auctions.stream()
			.filter(auction -> auction.getEndAuctionTime().isBefore(LocalDateTime.now()))
			.map(auction -> {
				if (!auction.hasBidder()) {
					auction.updateAuctionStatus(AuctionStatus.AUCTION_FAIL);
				} else {
					auction.updateAuctionStatus(AuctionStatus.AUCTION_SUCCESS);
				}
				Deal deal = dealRepository.save(new Deal(auction));
				return new DealEvent(deal);
			})
			.toList();
		dealEventRepository.saveAll(dealEvents);
		auctionRepository.saveAll(auctions);
	}

	@GetMapping("/deal")
	@Transactional
	public void update2() {
		log.info("deal");
		List<Deal> deals = dealRepository.findAllByDealStatus(DealStatus.DELIVERY_WAITING);
		deals.stream()
			.filter(deal -> deal.getDealDeadLine() != null)
			.filter(deal -> deal.getDealDeadLine().isBefore(LocalDateTime.now()))
			.forEach(deal -> deal.updateStatus(DealStatus.PURCHASE_CANCEL));

		Map<Long, DealDto> dealPriceMap = deals.stream()
			.collect(Collectors.toMap(
				Deal::getBuyerId,
				deal -> new DealDto(deal.getAuction().getId(), deal.getDealPrice())
			));

		Set<User> buyer = userRepository.findAllByIdIn(dealPriceMap.keySet());
		List<Payment> payments = buyer.stream()
			.map(user -> {
				DealDto dealDto = dealPriceMap.get(user.getId());
				user.increaseMoney(dealDto.getDealPrice());
				return new Payment(dealDto.getAuctionId(), dealDto.dealPrice, PaymentType.IN_CANCEL,
					LocalDateTime.now(), user);
			}).toList();

		paymentRepository.saveAll(payments);
		dealRepository.saveAll(deals);
	}

	@Getter
	private static class DealDto {
		private Long auctionId;
		private Long dealPrice;

		public DealDto(Long auctionId, Long dealPrice) {
			this.auctionId = auctionId;
			this.dealPrice = dealPrice;
		}
	}

	@GetMapping("/migration")
	@Transactional
	public void migration() {
		log.info("migration");
		Set<String> allAuctions = viewCacheRepository.getAllAuctions();
		if (allAuctions.isEmpty()) {
			return;
		}

		List<Long> auctionIds = allAuctions.stream().map(Long::valueOf).toList();
		List<View> views = viewRepository.findAllByAuctionIdIn(auctionIds);
		for (View view : views) {
			view.updateCount(viewCacheRepository.getViewCount(view.getAuctionId().toString()));
		}
		viewCacheRepository.removeAllUUID();
		viewCacheRepository.removeAllAuctions();
	}
}
