package com.betting.ground.auction.repository;

import static com.betting.ground.auction.domain.QAuction.*;
import static com.betting.ground.auction.domain.QAuctionImage.*;
import static com.betting.ground.auction.domain.QTag.*;
import static com.betting.ground.auction.domain.QView.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StopWatch;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
class AuctionRepositoryImplTest {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Test
	void offset_pagenagtion() {
		// given

		String keyword = "1";
		String filter = "title";
		Pageable pageable = PageRequest.of(10, 10);
		System.out.println("pageable.getP = " + pageable.getPageSize());
		System.out.println("pageable.getOffset() = " + pageable.getOffset());

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		JPQLQuery<String> auctionImage = getAuctionImage();

		List<AuctionInfo> auctions = jpaQueryFactory.selectDistinct(Projections.constructor(AuctionInfo.class,
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
			.where(likeKeyword(keyword + "%", filter))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(auction.id.desc())
			.fetch();

		Long count = jpaQueryFactory.select(
				auction.countDistinct()
			)
			.from(auction)
			.leftJoin(tag).on(tag.auction.eq(auction))
			.where(likeKeyword(keyword + "%", filter))
			.fetchOne();

		PageImpl<AuctionInfo> auctionInfos = new PageImpl<>(auctions, pageable, count);
		List<AuctionInfo> content = auctionInfos.getContent();
		stopWatch.stop();
		System.out.println("offset 걸린시간: " + stopWatch.getTotalTimeSeconds() + "s");

		for (AuctionInfo auctionInfo : content) {
			System.out.println("auctionInfo = " + auctionInfo);
		}

	}

	@Test
	void no_offset() {
		Long auctionId = 1990L;
		String filter = "title";
		String keyword = "1";
		Pageable pageable = PageRequest.of(0, 10);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		JPQLQuery<String> auctionImage = getAuctionImage();

		List<AuctionInfo> auctions = jpaQueryFactory.selectDistinct(Projections.constructor(AuctionInfo.class,
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
			.where(
				ltAuctionId(auctionId),
				likeKeyword(keyword + "%", filter)
			)
			.limit(pageable.getPageSize())
			.orderBy(auction.id.desc())
			.fetch();

		stopWatch.stop();
		System.out.println("no-offset 걸린시간: " + stopWatch.getTotalTimeSeconds() + "s");

		for (AuctionInfo auctionInfo : auctions) {
			System.out.println("auctionInfo = " + auctionInfo);
		}

	}

	@Test
	void no_covering_index() {
		Boolean progressFilter = true;
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("view")));

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
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

		setAuctionStatusFilter1(progressFilter, query, countQuery);

		setSorting1(pageable, query, countQuery);

		List<AuctionInfo> auctions = query.fetch();
		Long count = countQuery.fetchOne();

		PageImpl<AuctionInfo> auctionInfos = new PageImpl<>(auctions, pageable, count);

		stopWatch.stop();
		System.out.println("걸린시간: " + stopWatch.getTotalTimeSeconds() + "s");
		List<AuctionInfo> content = auctionInfos.getContent();
		for (AuctionInfo auctionInfo : content) {
			System.out.println("auctionInfo = " + auctionInfo);
		}

	}

	@Test
	void covering_index() {
		Boolean progressFilter = true;

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		JPQLQuery<String> auctionImage = getAuctionImage();
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("view")));

		JPAQuery<Long> query = jpaQueryFactory
			.select(auction.id)
			.from(auction)
			.leftJoin(view).on(auction.id.eq(view.auctionId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize());

		JPAQuery<Long> countQuery = jpaQueryFactory.select(
				auction.id.count()
			)
			.from(auction);

		setAuctionStatusFilter(progressFilter, query, countQuery);
		setSorting(pageable, query, countQuery);
		List<Long> ids = query.fetch();

		Long count = countQuery.fetchOne();

		List<AuctionInfo> fetch = jpaQueryFactory.select(Projections.constructor(AuctionInfo.class,
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
			.where(auction.id.in(ids))
			.fetch();

		PageImpl<AuctionInfo> auctionInfos = new PageImpl<>(fetch, pageable, count);

		stopWatch.stop();
		System.out.println("걸린시간: " + stopWatch.getTotalTimeSeconds() + "s");

		List<AuctionInfo> content = auctionInfos.getContent();
		for (AuctionInfo auctionInfo : content) {
			System.out.println("auctionInfo = " + auctionInfo);
		}

	}

	private static BooleanExpression likeKeyword(String keyword, String filter) {
		if (filter.equals("tag")) {
			return tag.tagName.like(keyword);
		} else if (filter.equals("title")) {
			return auction.title.like(keyword);
		}
		return null;
	}

	private static BooleanExpression ltAuctionId(Long auctionId) {
		if (auctionId == null) {
			return null;
		}
		return auction.id.lt(auctionId);
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

	private void setAuctionStatusFilter(Boolean progressFilter, JPAQuery<Long> query,
		JPAQuery<Long> countQuery) {
		if (Boolean.TRUE.equals(progressFilter)) {
			query.where(auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS));
			countQuery.where(auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS));
		}
	}

	private void setSorting(Pageable pageable, JPAQuery<Long> query, JPAQuery<Long> countQuery) {
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
			query.orderBy(auction.id.desc());
		}
	}

	private void setAuctionStatusFilter1(Boolean progressFilter, JPAQuery<AuctionInfo> query,
		JPAQuery<Long> countQuery) {
		if (Boolean.TRUE.equals(progressFilter)) {
			query.where(auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS));
			countQuery.where(auction.auctionStatus.eq(AuctionStatus.AUCTION_PROGRESS));
		}
	}

	private void setSorting1(Pageable pageable, JPAQuery<AuctionInfo> query, JPAQuery<Long> countQuery) {
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
}
