package com.betting.ground.auction.repository;

import com.betting.ground.auction.dto.response.AuctionInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.betting.ground.auction.domain.QAuction.auction;
import static com.betting.ground.auction.domain.QAuctionImage.auctionImage;
import static com.betting.ground.auction.domain.QTag.tag;

@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable) {
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        auction.currentPrice,
                        auction.instantPrice,
                        auction.endAuctionTime,
                        auction.viewCnt,
                        auctionImage
                ))
                .from(auction)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.createdAt.desc())
                .fetch();

        Long count = jpaQueryFactory.select(
                        auction.count()
                )
                .from(auction)
                .fetchOne();

        return new PageImpl<>(auctions, pageable, count);
    }

    @Override
    public PageImpl<AuctionInfo> findItemByOrderByEndAuctionTimeAsc(Pageable pageable) {
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        auction.currentPrice,
                        auction.instantPrice,
                        auction.endAuctionTime,
                        auction.viewCnt,
                        auctionImage
                ))
                .from(auction)
                .where(auction.endAuctionTime.gt(LocalDateTime.now()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.endAuctionTime.asc())
                .fetch();


        Long count = jpaQueryFactory.select(
                        auction.count()
                )
                .from(auction)
                .where(auction.endAuctionTime.gt(LocalDateTime.now()))
                .fetchOne();

        return new PageImpl<>(auctions, pageable, count);
    }

    @Override
    public PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable) {
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        auction.currentPrice,
                        auction.instantPrice,
                        auction.endAuctionTime,
                        auction.viewCnt,
                        auctionImage
                ))
                .from(auction)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.viewCnt.desc())
                .fetch();

        Long count = jpaQueryFactory.select(
                        auction.count()
                )
                .from(auction)
                .fetchOne();

        return new PageImpl<>(auctions, pageable, count);
    }

    @Override
    public PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable) {
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        auction.currentPrice,
                        auction.instantPrice,
                        auction.endAuctionTime,
                        auction.viewCnt,
                        auctionImage
                ))
                .from(auction)
                .leftJoin(tag).on(tag.auction.eq(auction))
                .where(auction.title.contains(keyword).or(tag.tagName.contains(keyword)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.createdAt.desc())
                .distinct()
                .fetch();


        Long count = jpaQueryFactory.select(
                        auction.countDistinct()
                )
                .from(auction)
                .leftJoin(tag).on(tag.auction.eq(auction))
                .where(auction.title.contains(keyword).or(tag.tagName.contains(keyword)))
                .fetchOne();

        return new PageImpl<>(auctions, pageable, count);
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
}
