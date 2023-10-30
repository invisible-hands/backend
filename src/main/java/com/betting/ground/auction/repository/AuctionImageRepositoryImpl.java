package com.betting.ground.auction.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.betting.ground.auction.domain.QAuctionImage.auctionImage;

@RequiredArgsConstructor
public class AuctionImageRepositoryImpl implements AuctionImageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void deleteByAuctionId(Long auctionId) {
        jpaQueryFactory.delete(auctionImage)
                .where(auctionImage.auction.id.eq(auctionId))
                .execute();
    }
}
