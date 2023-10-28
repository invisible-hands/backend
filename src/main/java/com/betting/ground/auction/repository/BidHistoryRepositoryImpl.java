package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.QBidHistory;
import com.betting.ground.auction.dto.BidInfo;
import com.betting.ground.user.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.betting.ground.auction.domain.QBidHistory.bidHistory;
import static com.betting.ground.user.domain.QUser.user;

@RequiredArgsConstructor
public class BidHistoryRepositoryImpl implements BidHistoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public PageImpl<BidInfo> findBidInfoByAuctionId(Long auctionId, Pageable pageable) {

        List<BidInfo> bidinfoList = jpaQueryFactory.select(Projections.constructor(BidInfo.class,
                        bidHistory.id, user.id, user.nickname, bidHistory.createdAt, bidHistory.price
                ))
                .from(bidHistory)
                .leftJoin(user).on(bidHistory.bidderId.eq(user.id))
                .where(bidHistory.auction.id.eq(auctionId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(bidHistory.price.desc())
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(bidHistory)
                .where(bidHistory.auction.id.eq(auctionId))
                .fetchOne();

        return new PageImpl<>(bidinfoList, pageable, count);
    }
}
