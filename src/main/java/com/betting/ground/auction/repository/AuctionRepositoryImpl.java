package com.betting.ground.auction.repository;

import static com.betting.ground.auction.domain.QAuction.*;
import static com.betting.ground.auction.domain.QAuctionImage.*;
import static com.betting.ground.auction.domain.QBidHistory.*;
import static com.betting.ground.auction.domain.QTag.*;
import static com.betting.ground.auction.domain.QView.*;
import static com.betting.ground.user.domain.QUser.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.AuctionDetailInfo;
import com.betting.ground.auction.dto.AuctionImageDto;
import com.betting.ground.auction.dto.SellerItemDto;
import com.betting.ground.auction.dto.TagDto;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.user.dto.login.LoginUser;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public PageImpl<AuctionInfo> findItems(Pageable pageable, Boolean progressFilter) {

		JPQLQuery<String> auctionImage = getAuctionImage();

		JPAQuery<AuctionInfo> query = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
				auction.id,
				auction.title,
				auction.currentPrice,
				auction.instantPrice,
				auction.duration,
				auction.createdAt,
				auction.endAuctionTime,
				auction.auctionStatus,
				view.cnt,
				auctionImage
			))
			.from(auction)
			.leftJoin(view).on(auction.id.eq(view.auctionId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		JPAQuery<Long> countQuery = jpaQueryFactory.select(
				auction.count()
			)
			.from(auction);

		setAuctionStatusFilter(progressFilter, query, countQuery);

		setSorting(pageable, query, countQuery);

		List<AuctionInfo> auctions = query.fetch();
		Long count = countQuery.fetchOne();

		return new PageImpl<>(auctions, pageable, count);
	}

	private void setAuctionStatusFilter(Boolean progressFilter, JPAQuery<AuctionInfo> query,
		JPAQuery<Long> countQuery) {
		if (Boolean.TRUE.equals(progressFilter)) {
			query.where(auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS));
			countQuery.where(auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS));
		}
	}

	private void setSorting(Pageable pageable, JPAQuery<AuctionInfo> query, JPAQuery<Long> countQuery) {
		Sort sort = pageable.getSort();
		if (sort.isSorted()) {
			for (Sort.Order order : sort) {
				if (order.getProperty().equals("deadline")) {
					query.where(auction.endAuctionTime.gt(LocalDateTime.now()));
					countQuery.where(auction.endAuctionTime.gt(LocalDateTime.now()));
				}
				query.orderBy(sortToExpression(order));
			}
		} else {
			query.orderBy(auction.createdAt.desc());
		}
	}

	private OrderSpecifier<?> sortToExpression(Sort.Order order) {
		if ("view".equalsIgnoreCase(order.getProperty())) {
			return order.getDirection() == Sort.Direction.ASC ? view.cnt.asc() : view.cnt.desc();
		} else if ("deadline".equalsIgnoreCase(order.getProperty())) {
			return order.getDirection() == Sort.Direction.ASC ? auction.endAuctionTime.asc() :
				auction.endAuctionTime.desc();
		} else {
			return order.getDirection() == Sort.Direction.ASC ? auction.createdAt.asc() : auction.createdAt.desc();
		}
	}

	@Override
	public PageImpl<AuctionInfo> findItemByKeywordByOrderByCreatedAtDesc(String keyword, Pageable pageable) {
		JPQLQuery<String> auctionImage = getAuctionImage();

		List<AuctionInfo> auctions = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
				auction.id,
				auction.title,
				auction.currentPrice,
				auction.instantPrice,
				auction.duration,
				auction.createdAt,
				auction.endAuctionTime,
				auction.auctionStatus,
				view.cnt,
				auctionImage
			))
			.from(auction)
			.leftJoin(view).on(auction.id.eq(view.auctionId))
			.leftJoin(tag).on(tag.auction.eq(auction))
			.where(auction.title.contains(keyword).or(tag.tagName.contains(keyword)))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.groupBy(auction, view)
			.orderBy(auction.createdAt.desc())
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
				auction.auctionStatus,
				bidHistory.bidderId.countDistinct(),
				view.cnt
			))
			.from(auction)
			.leftJoin(view).on(auction.id.eq(view.auctionId))
			.leftJoin(bidHistory).on(bidHistory.auction.eq(auction))
			.where(auction.id.eq(auctionId))
			.groupBy(auction, view)
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
	public PageImpl<SellerItemDto> findSellerItemBySellerId(Long sellerId, Pageable pageable) {

		List<SellerItemDto> findBiddingItem =
			jpaQueryFactory.select(Projections.constructor(SellerItemDto.class,
					auction.id, auction.title, auction.currentPrice, getAuctionImage(), auction.createdAt, auction.duration,
					auction.endAuctionTime, auction.auctionStatus))
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
