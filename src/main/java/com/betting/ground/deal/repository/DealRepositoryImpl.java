package com.betting.ground.deal.repository;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.dto.response.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.betting.ground.auction.domain.QAuction.auction;
import static com.betting.ground.auction.domain.QAuctionImage.auctionImage;
import static com.betting.ground.auction.domain.QBidHistory.bidHistory;
import static com.betting.ground.deal.domain.QDeal.deal;

@RequiredArgsConstructor
public class DealRepositoryImpl implements DealRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public PageImpl<PurchaseInfo> getAllPurchases(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate){
        List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
                        deal.auction.id,
                        deal.id,
                        getAuctionImage(),
                        auction.title,
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
        if(startDate == null || endDate == null) return null;

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
    public PageImpl<PurchaseInfo> getProgressPurchases(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate){
        List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
                        deal.auction.id,
                        deal.id,
                        getAuctionImage(),
                        auction.title,
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

    public PageImpl<PurchaseInfo> getCompletePurchases(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate){
        List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
                        deal.auction.id,
                        deal.id,
                        getAuctionImage(),
                        auction.title,
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
        List<BiddingInfo> bidding = jpaQueryFactory.select(new QBiddingInfo(
                        auction.id,
                        getAuctionImage(),
                        auction.title,
                        bidHistory.createdAt,
                        auction.endAuctionTime,
                        auction.currentPrice,
                        bidHistory.price,
                        auction.auctionStatus
                ))
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.id.eq(auction.id))
                .where(
                        bidHistory.price.eq(getMaxPrice(userId)),
                        bidPeriodTime(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bidHistory.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(auction.countDistinct())
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.id.eq(auction.id))
                .where(
                        bidHistory.price.eq(getMaxPrice(userId)),
                        bidPeriodTime(startDate, endDate)
                )
                .fetchOne();

        return new PageImpl<>(bidding, pageable, count);
    }

    private static JPQLQuery<Long> getMaxPrice(Long userId) {
        return JPAExpressions.select(
                        bidHistory.price.max()
                )
                .from(bidHistory)
                .where(
                        bidHistory.bidderId.eq(userId),
                        bidHistory.auction.id.eq(auction.id)
                );
    }

    private BooleanExpression bidPeriodTime(LocalDate startDate, LocalDate endDate) {
        if(startDate == null || endDate == null) return null;

        return bidHistory.createdAt.between(LocalDateTime.of(startDate, LocalTime.MIN),
                LocalDateTime.of(endDate, LocalTime.MAX));
    }

    @Override
    public PageImpl<BiddingInfo> getProgressBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        List<BiddingInfo> bidding = jpaQueryFactory.select(new QBiddingInfo(
                        auction.id,
                        getAuctionImage(),
                        auction.title,
                        bidHistory.createdAt,
                        auction.endAuctionTime,
                        auction.currentPrice,
                        bidHistory.price,
                        auction.auctionStatus
                ))
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.id.eq(auction.id))
                .where(
                        bidHistory.price.eq(getMaxPrice(userId)),
                        bidPeriodTime(startDate, endDate),
                        auctionStatusEq(AuctionStatus.AUCTION_PROGRESS)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bidHistory.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(auction.countDistinct())
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.id.eq(auction.id))
                .where(
                        bidHistory.price.eq(getMaxPrice(userId)),
                        bidPeriodTime(startDate, endDate),
                        auctionStatusEq(AuctionStatus.AUCTION_PROGRESS)
                )
                .fetchOne();

        return new PageImpl<>(bidding, pageable, count);
    }

    @Override
    public PageImpl<BiddingInfo> getCompleteBidding(long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        List<BiddingInfo> bidding = jpaQueryFactory.select(new QBiddingInfo(
                        auction.id,
                        getAuctionImage(),
                        auction.title,
                        bidHistory.createdAt,
                        auction.endAuctionTime,
                        auction.currentPrice,
                        bidHistory.price,
                        auction.auctionStatus
                ))
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.id.eq(auction.id))
                .where(
                        bidHistory.price.eq(getMaxPrice(userId)),
                        bidPeriodTime(startDate, endDate),
                        auctionStatusNe(AuctionStatus.AUCTION_PROGRESS)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bidHistory.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(auction.countDistinct())
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.id.eq(auction.id))
                .where(
                        bidHistory.price.eq(getMaxPrice(userId)),
                        bidPeriodTime(startDate, endDate),
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
    public PageImpl<SalesInfo> getProgressSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        List<SalesInfo> sales = jpaQueryFactory.select(new QSalesInfo(
                        auction.id,
                        deal.id,
                        getAuctionImage(),
                        auction.title,
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
                        dealStatusEq(DealStatus.PURCHASE_COMPLETE_WAITING)
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
                        dealStatusEq(DealStatus.PURCHASE_COMPLETE_WAITING),
                        dealStatusEq(DealStatus.PURCHASE_CANCEL)
                )
                .fetchOne();

        return new PageImpl<>(sales, pageable, count);
    }

    @Override
    public PageImpl<SalesInfo> getCompleteSales(Long userId, Pageable pageable, LocalDate startDate, LocalDate endDate) {
        List<SalesInfo> sales = jpaQueryFactory.select(new QSalesInfo(
                        auction.id,
                        deal.id,
                        getAuctionImage(),
                        auction.title,
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
                        dealStatusEq(DealStatus.PURCHASE_COMPLETE),
                        dealStatusEq(DealStatus.PURCHASE_CANCEL)
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
                        dealStatusEq(DealStatus.PURCHASE_COMPLETE)
                )
                .fetchOne();

        return new PageImpl<>(sales, pageable, count);
    }
}
