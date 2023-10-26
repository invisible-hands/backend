package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.*;
import com.betting.ground.user.dto.response.PurchaseInfo;
import com.betting.ground.user.dto.response.QPurchaseInfo;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.betting.ground.auction.domain.QAuction.auction;
import static com.betting.ground.auction.domain.QAuctionImage.auctionImage;
import static com.betting.ground.auction.domain.QBidHistory.bidHistory;
import static com.betting.ground.auction.domain.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public PageImpl<PurchaseInfo> getAllPurchases(Long userId, Pageable pageable){
        List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
                        item.auction.id,
                        getAuctionImage(),
                        auction.title,
                        item.closeAuctionTime,
                        getMaxPrice(),
                        item.auctionStatus
                ))
                .from(item)
                .leftJoin(auction).on(item.auction.id.eq(auction.id))
                .where(item.buyerId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.closeAuctionTime.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(item.countDistinct())
                .from(item)
                .where(item.buyerId.eq(userId))
                .fetchOne();

        return new PageImpl<>(purchases, pageable, count);
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

    private static JPQLQuery<Long> getMaxPrice(){
        return JPAExpressions.select(
                    bidHistory.price.max()
                )
                .from(bidHistory)
                .where(bidHistory.auction.id.eq(auction.id));
    }

    public PageImpl<PurchaseInfo> getBeforePurchases(long userId, Pageable pageable){
        List<PurchaseInfo> purchases = jpaQueryFactory.select(new QPurchaseInfo(
                        item.auction.id,
                        getAuctionImage(),
                        auction.title,
                        item.closeAuctionTime,
                        item.finalPrice,
                        item.auctionStatus
                ))
                .from(item)
                .leftJoin(auction).on(item.auction.id.eq(auction.id))
                .where(item.buyerId.eq(userId).and(item.auctionStatus.eq(AuctionStatus.DELIVERY_WAITING)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.closeAuctionTime.desc())
                .fetch();

        Long count = jpaQueryFactory
                .select(item.countDistinct())
                .from(item)
                .where(item.buyerId.eq(userId).and(item.auctionStatus.eq(AuctionStatus.DELIVERY_WAITING)))
                .fetchOne();

        return new PageImpl<>(purchases, pageable, count);
    }
}
