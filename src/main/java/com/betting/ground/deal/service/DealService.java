package com.betting.ground.deal.service;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionImage;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.BidHistory;
import com.betting.ground.auction.repository.AuctionImageRepository;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.auction.repository.BidHistoryRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.dto.response.BiddingInfo;
import com.betting.ground.deal.dto.response.BiddingInfoResponse;
import com.betting.ground.deal.dto.response.DealCountResponse;
import com.betting.ground.deal.dto.response.PurchaseInfo;
import com.betting.ground.deal.dto.response.PurchaseInfoResponse;
import com.betting.ground.deal.dto.response.SalesInfo;
import com.betting.ground.deal.dto.response.SalesInfoResponse;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import com.betting.ground.user.domain.Payment;
import com.betting.ground.user.domain.PaymentType;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.repository.PaymentRepository;
import com.betting.ground.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DealService {

	private final DealRepository dealRepository;
	private final AuctionRepository auctionRepository;
	private final DealEventRepository dealEventRepository;
	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;
	private final AuctionImageRepository auctionImageRepository;
	private final BidHistoryRepository bidHistoryRepository;

	@Transactional(readOnly = true)
	public PurchaseInfoResponse getAllPurchases(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.PURCHASE_COMPLETE, DealStatus.PURCHASE_CANCEL,
			DealStatus.DELIVERY_WAITING, DealStatus.PURCHASE_COMPLETE_WAITING);

		return getPurchaseInfoResponse(userId, pageable, startDate, endDate, status);
	}

	@Transactional(readOnly = true)
	public PurchaseInfoResponse getProgressPurchases(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.DELIVERY_WAITING, DealStatus.PURCHASE_COMPLETE_WAITING);

		return getPurchaseInfoResponse(userId, pageable, startDate, endDate, status);
	}

	@Transactional(readOnly = true)
	public PurchaseInfoResponse getCompletePurchases(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.PURCHASE_COMPLETE, DealStatus.PURCHASE_CANCEL);

		return getPurchaseInfoResponse(userId, pageable, startDate, endDate, status);
	}

	public void purchaseComplete(Long dealId) {
		Deal deal = dealRepository.findById(dealId).orElseThrow(
			() -> new GlobalException(ErrorCode.DEAL_NOT_FOUND)
		);

		if (deal.getDealStatus() != DealStatus.PURCHASE_COMPLETE_WAITING) {
			throw new GlobalException(ErrorCode.CAN_NOT_COMPLETE);
		}

		deal.updateStatus(DealStatus.PURCHASE_COMPLETE);

		User user = userRepository.findById(deal.getSellerId()).orElseThrow(
			() -> new GlobalException(ErrorCode.USER_NOT_FOUND)
		);

		user.settle(deal.getDealPrice());
		Payment payment = new Payment(deal, PaymentType.IN_SETTLEMENT, user);
		paymentRepository.save(payment);
		dealEventRepository.save(new DealEvent(deal));
	}

	@Transactional(readOnly = true)
	public BiddingInfoResponse getAllBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		int size = bidHistoryRepository.findByBidderIdAndCreatedAtBetween(userId, convertStartDate(startDate),
				convertEndDate(endDate))
			.stream()
			.map(history -> history.getAuction().getId())
			.distinct()
			.collect(toList())
			.size();

		List<BidHistory> bidHistory = bidHistoryRepository.findByBidderIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId,
			pageable, convertStartDate(startDate), convertEndDate(endDate));

		Set<Long> auctionIds = bidHistory.stream()
			.map(history -> history.getAuction().getId())
			.collect(toSet());

		Map<Long, Optional<BidHistory>> histories = bidHistory.stream()
			.collect(groupingBy(history -> history.getAuction().getId(),
				maxBy(Comparator.comparingLong(BidHistory::getPrice))));

		Map<Long, List<Auction>> auctions = auctionRepository.findByIdIn(auctionIds.stream().toList())
			.stream().collect(groupingBy(Auction::getId));

		Map<Long, List<AuctionImage>> images = auctionImageRepository.findByAuctionIdIn(auctionIds.stream().toList())
			.stream().collect(groupingBy(image -> image.getAuction().getId()));

		List<BiddingInfo> collect = auctionIds.stream()
			.map(id -> new BiddingInfo(
				ListUtils.emptyIfNull(auctions.get(id)).get(0),
				ListUtils.emptyIfNull(images.get(id)).get(0),
				histories.get(id).orElseThrow()
			))
			.collect(toList());

		return new BiddingInfoResponse(new PageImpl<>(collect, pageable, size));
	}

	@Transactional(readOnly = true)
	public BiddingInfoResponse getProgressBidding(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {

		List<BidHistory> bidHistory = bidHistoryRepository.findByBidderIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId,
			convertStartDate(startDate), convertEndDate(endDate));

		Set<Long> auctionIds = bidHistory.stream()
			.map(history -> history.getAuction().getId())
			.collect(toSet());

		Map<Long, Optional<BidHistory>> histories = bidHistory.stream()
			.collect(groupingBy(history -> history.getAuction().getId(),
				maxBy(Comparator.comparingLong(BidHistory::getPrice))));

		Map<Long, List<Auction>> auctions = auctionRepository.findByIdInAndAuctionStatus(auctionIds.stream().toList(),
				AuctionStatus.AUCTION_PROGRESS)
			.stream()
			.collect(groupingBy(Auction::getId));

		Map<Long, List<AuctionImage>> images = auctionImageRepository.findByAuctionIdIn(auctionIds.stream().toList())
			.stream().collect(groupingBy(image -> image.getAuction().getId()));

		int fromIndex = (pageable.getPageNumber() * pageable.getPageSize());
		int toIndex = (pageable.getPageNumber() + 1) * pageable.getPageSize();

		List<BiddingInfo> collect = auctionIds.stream()
			.filter(id -> auctions.get(id) != null)
			.map(id -> new BiddingInfo(
				ListUtils.emptyIfNull(auctions.get(id)).get(0),
				ListUtils.emptyIfNull(images.get(id)).get(0),
				histories.get(id).orElseThrow()
			))
			.collect(toList());
		collect = getSubList(fromIndex, toIndex, collect);

		return new BiddingInfoResponse(new PageImpl<>(collect, pageable, auctions.size()));
	}

	@Transactional(readOnly = true)
	public BiddingInfoResponse getCompleteBidding(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {

		List<BidHistory> bidHistory = bidHistoryRepository.findByBidderIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId,
			convertStartDate(startDate), convertEndDate(endDate));

		Set<Long> auctionIds = bidHistory.stream()
			.map(history -> history.getAuction().getId())
			.collect(toSet());

		Map<Long, Optional<BidHistory>> histories = bidHistory.stream()
			.collect(groupingBy(history -> history.getAuction().getId(),
				maxBy(Comparator.comparingLong(BidHistory::getPrice))));

		Map<Long, List<Auction>> auctions = auctionRepository.findByIdInAndAuctionStatus(auctionIds.stream().toList(),
				AuctionStatus.AUCTION_SUCCESS)
			.stream()
			.collect(groupingBy(Auction::getId));

		Map<Long, List<AuctionImage>> images = auctionImageRepository.findByAuctionIdIn(auctionIds.stream().toList())
			.stream().collect(groupingBy(image -> image.getAuction().getId()));

		int fromIndex = (pageable.getPageNumber() * pageable.getPageSize());
		int toIndex = (pageable.getPageNumber() + 1) * pageable.getPageSize();

		List<BiddingInfo> collect = auctionIds.stream()
			.filter(id -> auctions.get(id) != null)
			.map(id -> new BiddingInfo(
				ListUtils.emptyIfNull(auctions.get(id)).get(0),
				ListUtils.emptyIfNull(images.get(id)).get(0),
				histories.get(id).orElseThrow()
			))
			.collect(toList());
		collect = getSubList(fromIndex, toIndex, collect);

		return new BiddingInfoResponse(new PageImpl<>(collect, pageable, auctions.size()));
	}

	@NotNull
	private List<BiddingInfo> getSubList(int fromIndex, int toIndex, List<BiddingInfo> collect) {
		toIndex = toIndex > collect.size() ? collect.size() : toIndex;

		if (fromIndex > toIndex) {
			return new ArrayList<>();
		}
		return collect.subList(fromIndex, toIndex);
	}

	@Transactional(readOnly = true)
	public SalesInfoResponse getAllSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.SALE_FAIL, DealStatus.PURCHASE_COMPLETE_WAITING,
			DealStatus.PURCHASE_COMPLETE, DealStatus.PURCHASE_CANCEL, DealStatus.DELIVERY_WAITING);

		return getSalesInfoResponse(userId, pageable, startDate, endDate, status);
	}

	@Transactional(readOnly = true)
	public SalesInfoResponse getBeforeSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.DELIVERY_WAITING);

		return getSalesInfoResponse(userId, pageable, startDate, endDate, status);
	}

	@Transactional(readOnly = true)
	public SalesInfoResponse getProgressSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.PURCHASE_COMPLETE_WAITING);

		return getSalesInfoResponse(userId, pageable, startDate, endDate, status);
	}

	@Transactional(readOnly = true)
	public SalesInfoResponse getCompleteSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<DealStatus> status = List.of(DealStatus.PURCHASE_COMPLETE,
			DealStatus.PURCHASE_CANCEL, DealStatus.SALE_FAIL);

		return getSalesInfoResponse(userId, pageable, startDate, endDate, status);
	}

	@Transactional(readOnly = true)
	public DealCountResponse getPurchasesCount(Long userId) {
		int progress = 0;
		int complete = 0;

		List<Deal> list = dealRepository.findAllByBuyerId(userId);
		for (Deal deal : list) {
			if (deal.getDealStatus().equals(DealStatus.PURCHASE_COMPLETE) || deal.getDealStatus()
				.equals(DealStatus.PURCHASE_CANCEL)) {
				complete++;
			} else {
				progress++;
			}
		}

		return DealCountResponse.purchases(progress + complete, progress, complete);
	}

	@Transactional(readOnly = true)
	public DealCountResponse getBiddingCount(Long userId) {
		int progress = 0;
		int complete = 0;

		List<AuctionStatus> list = auctionRepository.getAuctionByBidderId(userId);

		for (AuctionStatus status : list) {
			if (status.equals(AuctionStatus.AUCTION_PROGRESS)) {
				progress++;
			} else {
				complete++;
			}
		}

		return DealCountResponse.bids(progress + complete, progress, complete);
	}

	@Transactional(readOnly = true)
	public DealCountResponse getSalesCount(Long userId) {
		int before = 0;
		int progress = 0;
		int complete = 0;

		List<Deal> sales = dealRepository.findAllBySellerId(userId);

		for (Deal deal : sales) {
			if (deal.getDealStatus().equals(DealStatus.DELIVERY_WAITING)) {
				before++;
			} else if (deal.getDealStatus().equals(DealStatus.PURCHASE_COMPLETE_WAITING)) {
				progress++;
			} else {
				complete++;
			}
		}

		return DealCountResponse.sales(before + progress + complete, before, progress, complete);
	}

	private LocalDateTime convertStartDate(LocalDate startDate) {
		return LocalDateTime.of(startDate, LocalTime.MIN);
	}

	private LocalDateTime convertEndDate(LocalDate endDate) {
		return LocalDateTime.of(endDate, LocalTime.MAX);
	}

	@NotNull
	private PurchaseInfoResponse getPurchaseInfoResponse(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate, List<DealStatus> status) {
		int size = dealRepository.findByBuyerIdAndDealStatusInAndDealTimeBetween(userId,
			status, convertStartDate(startDate), convertEndDate(endDate)).size();

		List<Deal> deals = dealRepository.findByBuyerIdAndDealStatusInAndDealTimeBetweenOrderByDealTimeDesc(userId,
			pageable, status, convertStartDate(startDate), convertEndDate(endDate));
		List<Long> auctionIds = deals.stream().map(deal -> deal.getAuction().getId()).collect(toList());
		Map<Long, List<Auction>> auctions = auctionRepository.findByIdIn(auctionIds).stream()
			.collect(groupingBy(Auction::getId));
		Map<Long, List<AuctionImage>> images = auctionImageRepository.findByAuctionIdIn(auctionIds).stream()
			.collect(groupingBy(image -> image.getAuction().getId()));

		List<PurchaseInfo> collect = deals.stream()
			.map(deal -> new PurchaseInfo(
				deal,
				ListUtils.emptyIfNull(auctions.get(deal.getAuction().getId())).get(0),
				ListUtils.emptyIfNull(images.get(deal.getAuction().getId())).get(0)
			))
			.collect(Collectors.toList());

		return new PurchaseInfoResponse(new PageImpl<>(collect, pageable, size));
	}

	@NotNull
	private SalesInfoResponse getSalesInfoResponse(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate, List<DealStatus> status) {
		int size = dealRepository.findBySellerIdAndDealStatusInAndDealTimeBetween(userId,
			status,
			convertStartDate(startDate), convertEndDate(endDate)).size();

		List<Deal> deals = dealRepository.findBySellerIdAndDealStatusInAndDealTimeBetweenOrderByDealTimeDesc(userId,
			pageable,
			status,
			convertStartDate(startDate), convertEndDate(endDate));

		List<Long> auctionIds = deals.stream().map(deal -> deal.getAuction().getId()).collect(toList());
		Map<Long, List<Auction>> auctions = auctionRepository.findByIdIn(auctionIds).stream()
			.collect(groupingBy(Auction::getId));
		Map<Long, List<AuctionImage>> images = auctionImageRepository.findByAuctionIdIn(auctionIds).stream()
			.collect(groupingBy(image -> image.getAuction().getId()));

		List<SalesInfo> collect = deals.stream()
			.map(deal -> new SalesInfo(
				deal,
				ListUtils.emptyIfNull(auctions.get(deal.getAuction().getId())).get(0),
				ListUtils.emptyIfNull(images.get(deal.getAuction().getId())).get(0)
			))
			.collect(toList());

		return new SalesInfoResponse(new PageImpl<>(collect, pageable, size));
	}
}
