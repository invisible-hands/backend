package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.QTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.betting.ground.auction.domain.QTag.tag;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public void deleteByAcutionId(Long auctionId) {
        jpaQueryFactory.delete(tag)
                .where(tag.auction.id.eq(auctionId))
                .execute();
    }
}
