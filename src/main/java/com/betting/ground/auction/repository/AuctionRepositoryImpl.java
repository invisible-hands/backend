package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.AuctionDetailInfo;
import com.betting.ground.auction.dto.AuctionImageDto;
import com.betting.ground.auction.dto.SellerItemDto;
import com.betting.ground.auction.dto.TagDto;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public ItemDetailDto findDetailAuctionById(LoginUser loginUser, Long auctionId) {
        AuctionDetailInfo auctionDetailInfo = jpaQueryFactory.select(Projections.constructor(AuctionDetailInfo.class,
                        auction.id,
                        auction.user.id,
                        auction.title,
                        auction.content,
                        auction.itemCondition,
                        auction.currentPrice,
                        auction.instantPrice,
                        auction.createdAt,
                        auction.endAuctionTime,
                        auction.duration,
                        bidHistory.count(),
                        auction.viewCnt
                ))
                .from(auction)
                .leftJoin(bidHistory).on(bidHistory.auction.eq(auction))
                .where(auction.id.eq(auctionId))
                .fetchOne();

        List<AuctionImageDto> images = jpaQueryFactory.select(Projections.constructor(AuctionImageDto.class,
                        auctionImage.id,
                        auctionImage.imageUrl
                ))
                .from(auctionImage)
                .where(auctionImage.auction.id.eq(auctionId))
                .fetch();

        List<TagDto> tags = jpaQueryFactory.select(Projections.constructor(TagDto.class,
                        tag.id,
                        tag.tagName
                ))
                .from(tag)
                .where(tag.auction.id.eq(auctionId))
                .fetch();

        boolean authorCheck = loginUser != null && auctionDetailInfo.getSellerId().equals(loginUser.getUser().getId());

        return new ItemDetailDto(auctionDetailInfo, images, tags, authorCheck);
    }

    @Override
    public Optional<User> findSellerById(Long auctionId) {
        User seller = jpaQueryFactory.select(user)
                .from(auction)
                .leftJoin(auction.user, user)
                .where(auction.id.eq(auctionId))
                .fetchOne();

        return Optional.ofNullable(seller);
    }

    @Override
    public PageImpl<SellerItemDto> findSellerItemBySellerId(Long sellerId, Pageable pageable) {

        List<SellerItemDto> findBiddingItem =
                jpaQueryFactory.select(Projections.constructor(SellerItemDto.class,
                                auction.id, auction.title, auction.currentPrice, getAuctionImage(), auction.createdAt, auction.duration))
                        .distinct()
                        .from(auction)
                        .leftJoin(auction.user, user)
                        .where(user.id.eq(sellerId),
                                auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS))
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(auction)
                .leftJoin(auction.user, user)
                .where(user.id.eq(sellerId),
                        auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS))
                .fetchOne();


        return new PageImpl<>(findBiddingItem, pageable, count);
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

    @Override
    public BidInfoResponse getBidInfo(Long auctionId, Long userId){
        return jpaQueryFactory.select(Projections.constructor(BidInfoResponse.class,
                        getAuctionImage(),
                        auction.title,
                        auction.currentPrice,
                        auction.endAuctionTime,
                        user.money
                ))
                .from(auction)
                .leftJoin(user).on(auction.user.id.eq(userId))
                .where(auction.id.eq(auctionId))
                .fetchOne();
    }

    public List<AuctionStatus> getAuctionByBidderId(Long userId) {
        return jpaQueryFactory.select(
                        auction.auctionStatus
                )
                .from(auction)
                .where(auction.id.in(
                        JPAExpressions.select(
                                        bidHistory.auction.id
                                )
                                .from(bidHistory)
                                .where(bidHistory.bidderId.eq(userId))
                                .distinct()
                ))
                .fetch();
    }
}
