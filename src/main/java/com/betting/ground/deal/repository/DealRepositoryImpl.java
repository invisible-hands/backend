package com.betting.ground.deal.repository;

import static com.betting.ground.auction.domain.QAuction.*;
import static com.betting.ground.auction.domain.QAuctionImage.*;
import static com.betting.ground.auction.domain.QBidHistory.*;
import static com.betting.ground.deal.domain.QDeal.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.dto.response.BiddingInfo;
import com.betting.ground.deal.dto.response.PurchaseInfo;
import com.betting.ground.deal.dto.response.QBiddingInfo;
import com.betting.ground.deal.dto.response.QPurchaseInfo;
import com.betting.ground.deal.dto.response.QSalesInfo;
import com.betting.ground.deal.dto.response.SalesInfo;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DealRepositoryImpl implements DealRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	public PageImpl<PurchaseInfo> getAllPurchases(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
				deal.auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.buyerId.eq(userId),
				dealPeriodDate(startDate, endDate)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		Long count = jpaQueryFactory
			.select(deal.countDistinct())
			.from(deal)
			.where(
				deal.buyerId.eq(userId),
				dealPeriodDate(startDate, endDate)
			)
			.fetchOne();

		return new PageImpl<>(purchases, pageable, count);
	}

	private BooleanExpression dealPeriodDate(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null)
			return null;

		return deal.dealTime.between(LocalDateTime.of(startDate, LocalTime.MIN),
			LocalDateTime.of(endDate, LocalTime.MAX));
	}

	private static JPQLQuery<String> getAuctionImage() {
		return JPAExpressions.select(
				auctionImage.imageUrl
			)
			.from(auctionImage)
			.where(auctionImage.id.eq(JPAExpressions.select(
						auctionImage.id.min()
					)
					.from(auctionImage)
					.where(auctionImage.auction.eq(auction))
			));
	}

	public PageImpl<PurchaseInfo> getProgressPurchases(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
				deal.auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.buyerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.PURCHASE_COMPLETE_WAITING).or(dealStatusEq(DealStatus.DELIVERY_WAITING))
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		Long count = jpaQueryFactory
			.select(deal.countDistinct())
			.from(deal)
			.where(
				deal.buyerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.PURCHASE_COMPLETE_WAITING).or(dealStatusEq(DealStatus.DELIVERY_WAITING))
			)
			.fetchOne();

		return new PageImpl<>(purchases, pageable, count);
	}

	private BooleanExpression dealStatusEq(DealStatus status) {
		return status != null ? deal.dealStatus.eq(status) : null;
	}

	public PageImpl<PurchaseInfo> getCompletePurchases(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
				deal.auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.buyerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.PURCHASE_COMPLETE).or(dealStatusEq(DealStatus.PURCHASE_CANCEL))
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		Long count = jpaQueryFactory
			.select(deal.countDistinct())
			.from(deal)
			.where(
				deal.buyerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.PURCHASE_COMPLETE).or(dealStatusEq(DealStatus.PURCHASE_CANCEL))
			)
			.fetchOne();

		return new PageImpl<>(purchases, pageable, count);
	}

	@Override
	public PageImpl<BiddingInfo> getAllBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<Long> ids = jpaQueryFactory.select(auction.id).distinct()
			.from(bidHistory)
			.where(
				bidHistory.bidderId.eq(userId),
				bidPeriodTime(startDate, endDate)
			)
			.fetch();

		List<BiddingInfo> bidding = jpaQueryFactory.select(new QBiddingInfo(
				auction.id,
				getAuctionImage(),
				auction.title,
				bidHistory.createdAt,
				auction.createdAt,
				auction.duration,
				auction.endAuctionTime,
				auction.currentPrice,
				bidHistory.price,
				auction.auctionStatus
			))
			.from(auction)
			.leftJoin(bidHistory).on(bidHistory.id.eq(getMaxPrice(userId)))
			.where(
				auction.id.in(ids)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(bidHistory.createdAt.desc())
			.fetch();

		return new PageImpl<>(bidding, pageable, ids.size());
	}

	private static JPQLQuery<Long> getMaxPrice(Long userId) {
		return JPAExpressions.select(
				bidHistory.id.max()
			)
			.from(bidHistory)
			.where(
				bidHistory.bidderId.eq(userId),
				bidHistory.auction.id.eq(auction.id)
			)
			.groupBy(bidHistory.auction.id);
	}

	private BooleanExpression bidPeriodTime(LocalDate startDate, LocalDate endDate) {
		if (startDate == null || endDate == null)
			return null;

		return bidHistory.createdAt.between(LocalDateTime.of(startDate, LocalTime.MIN),
			LocalDateTime.of(endDate, LocalTime.MAX));
	}

	@Override
	public PageImpl<BiddingInfo> getProgressBidding(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<Long> ids = jpaQueryFactory.select(auction.id).distinct()
			.from(bidHistory)
			.where(
				bidHistory.bidderId.eq(userId),
				bidPeriodTime(startDate, endDate)
			)
			.fetch();

		List<BiddingInfo> bidding = jpaQueryFactory.select(new QBiddingInfo(
				auction.id,
				getAuctionImage(),
				auction.title,
				bidHistory.createdAt,
				auction.createdAt,
				auction.duration,
				auction.endAuctionTime,
				auction.currentPrice,
				bidHistory.price,
				auction.auctionStatus
			))
			.from(auction)
			.leftJoin(bidHistory).on(bidHistory.id.eq(getMaxPrice(userId)))
			.where(
				auction.id.in(ids),
				auctionStatusEq(AuctionStatus.AUCTION_PROGRESS)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(bidHistory.createdAt.desc())
			.fetch();

		Long count = jpaQueryFactory
			.select(auction.countDistinct())
			.from(auction)
			.where(
				auction.id.in(ids),
				auctionStatusEq(AuctionStatus.AUCTION_PROGRESS)
			)
			.fetchOne();

		return new PageImpl<>(bidding, pageable, count);
	}

	@Override
	public PageImpl<BiddingInfo> getCompleteBidding(long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {
		List<Long> ids = jpaQueryFactory.select(auction.id).distinct()
			.from(bidHistory)
			.where(
				bidHistory.bidderId.eq(userId),
				bidPeriodTime(startDate, endDate)
			)
			.fetch();

		List<BiddingInfo> bidding = jpaQueryFactory.select(new QBiddingInfo(
				auction.id,
				getAuctionImage(),
				auction.title,
				bidHistory.createdAt,
				auction.createdAt,
				auction.duration,
				auction.endAuctionTime,
				auction.currentPrice,
				bidHistory.price,
				auction.auctionStatus
			))
			.from(auction)
			.leftJoin(bidHistory).on(bidHistory.id.eq(getMaxPrice(userId)))
			.where(
				auction.id.in(ids),
				auctionStatusNe(AuctionStatus.AUCTION_PROGRESS)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(bidHistory.createdAt.desc())
			.fetch();

		Long count = jpaQueryFactory
			.select(auction.countDistinct())
			.from(auction)
			.where(
				auction.id.in(ids),
				auctionStatusNe(AuctionStatus.AUCTION_PROGRESS)
			)
			.fetchOne();

		return new PageImpl<>(bidding, pageable, count);
	}

	private BooleanExpression auctionStatusEq(AuctionStatus status) {
		return status != null ? auction.auctionStatus.eq(status) : null;
	}

	private BooleanExpression auctionStatusNe(AuctionStatus status) {
		return status != null ? auction.auctionStatus.ne(status) : null;
	}

	@Override
	public PageImpl<SalesInfo> getAllSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<SalesInfo> sales = jpaQueryFactory.select(new QSalesInfo(
				auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealDeadLine,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.sellerId.eq(userId),
				dealPeriodDate(startDate, endDate)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		Long count = jpaQueryFactory.select(deal.countDistinct())
			.from(deal)
			.where(
				deal.sellerId.eq(userId),
				dealPeriodDate(startDate, endDate)
			)
			.fetchOne();

		return new PageImpl<>(sales, pageable, count);
	}

	@Override
	public PageImpl<SalesInfo> getBeforeSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
		List<SalesInfo> sales = jpaQueryFactory.select(new QSalesInfo(
				auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealDeadLine,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.sellerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.DELIVERY_WAITING)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		Long count = jpaQueryFactory.select(deal.countDistinct())
			.from(deal)
			.where(
				deal.sellerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.DELIVERY_WAITING)
			)
			.fetchOne();

		return new PageImpl<>(sales, pageable, count);
	}

	@Override
	public PageImpl<SalesInfo> getProgressSales(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {

		List<Long> ids = jpaQueryFactory.select(deal.id)
			.from(deal)
			.where(
				deal.sellerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.PURCHASE_COMPLETE_WAITING)
			)
			.fetch();

		List<SalesInfo> sales = jpaQueryFactory.select(new QSalesInfo(
				auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealDeadLine,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.sellerId.in(ids)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		return new PageImpl<>(sales, pageable, ids.size());
	}

	@Override
	public PageImpl<SalesInfo> getCompleteSales(Long userId, Pageable pageable, LocalDate startDate,
		LocalDate endDate) {

		List<Long> ids = jpaQueryFactory.select(deal.id)
			.from(deal)
			.where(
				deal.sellerId.eq(userId),
				dealPeriodDate(startDate, endDate),
				dealStatusEq(DealStatus.PURCHASE_COMPLETE).or(
					dealStatusEq(DealStatus.PURCHASE_CANCEL).or(dealStatusEq(DealStatus.SALE_FAIL)))
			)
			.fetch();

		List<SalesInfo> sales = jpaQueryFactory.select(new QSalesInfo(
				auction.id,
				deal.id,
				getAuctionImage(),
				auction.title,
				auction.createdAt,
				auction.duration,
				deal.dealTime,
				deal.dealDeadLine,
				deal.dealPrice,
				deal.dealStatus
			))
			.from(deal)
			.leftJoin(auction).on(deal.auction.id.eq(auction.id))
			.where(
				deal.id.in(ids)
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(deal.dealTime.desc())
			.fetch();

		return new PageImpl<>(sales, pageable, ids.size());
	}
}
