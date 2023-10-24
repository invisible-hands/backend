package com.betting.ground.auction.repository;

import com.betting.ground.auction.dto.AuctionImageDto;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.betting.ground.auction.domain.QAuction.auction;
import static com.betting.ground.auction.domain.QAuctionImage.auctionImage;
import static com.betting.ground.auction.domain.QBidHistory.bidHistory;
import static com.betting.ground.auction.domain.QTag.tag;
import static com.betting.ground.user.domain.QUser.user;

@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public PageImpl<AuctionInfo> findItemByOrderByCreatedAtDesc(Pageable pageable) {
        JPQLQuery<Long> currentPrice = getCurrentPriceFromBidHistory();
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        currentPrice,
                        auction.instantPrice,
                        auction.createdAt,
                        auction.endAuctionTime,
                        auction.duration,
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
        JPQLQuery<Long> currentPrice = getCurrentPriceFromBidHistory();
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        currentPrice,
                        auction.instantPrice,
                        auction.createdAt,
                        auction.endAuctionTime,
                        auction.duration,
                        auction.viewCnt,
                        auctionImage
                ))
                .from(auction)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(auction.endAuctionTime.asc())
                .fetch();


        Long count = jpaQueryFactory.select(
                        auction.count()
                )
                .from(auction)
                .fetchOne();

        return new PageImpl<>(auctions, pageable, count);
    }

    @Override
    public PageImpl<AuctionInfo> findItemByOrderByViewCntDesc(Pageable pageable) {
        JPQLQuery<Long> currentPrice = getCurrentPriceFromBidHistory();
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        currentPrice,
                        auction.instantPrice,
                        auction.createdAt,
                        auction.endAuctionTime,
                        auction.duration,
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
        JPQLQuery<Long> currentPrice = getCurrentPriceFromBidHistory();
        JPQLQuery<String> auctionImage = getAuctionImage();

        List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
                        auction.id,
                        auction.title,
                        currentPrice,
                        auction.instantPrice,
                        auction.createdAt,
                        auction.endAuctionTime,
                        auction.duration,
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

    private static JPQLQuery<Long> getCurrentPriceFromBidHistory() {
        return JPAExpressions.select(bidHistory.price)
                .from(bidHistory)
                .where(bidHistory.id.eq(JPAExpressions.select(
                                        bidHistory.id.max()
                                )
                                .from(bidHistory)
                                .where(bidHistory.auction.eq(auction))
                ));
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

    public List<AuctionImageDto> getAuctionImages(){
        return jpaQueryFactory.select(auctionImage)
                .from(auctionImage)
                .where(auctionImage.auction.id.eq(auction.id))
                .fetch()
                .stream()
                .map(AuctionImageDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public BidInfoResponse getBidInfo(Long auctionId, Long userId){
        JPQLQuery<Long> currentPrice = getCurrentPriceFromBidHistory();

        BidInfoResponse bidInfoResponse = jpaQueryFactory.select(Projections.constructor(BidInfoResponse.class,
                        auction.title,
                        currentPrice,
                        auction.endAuctionTime,
                        user.money
                ))
                .from(auction)
                .leftJoin(user).on(auction.user.id.eq(userId))
                .where(auction.id.eq(auctionId))
                .fetchOne();

        if(bidInfoResponse != null)
            bidInfoResponse.setImages(getAuctionImages());

        return bidInfoResponse;
    }
}
