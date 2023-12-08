package com.betting.ground.auction.repository;

import static com.betting.ground.auction.domain.QAuction.*;
import static com.betting.ground.auction.domain.QAuctionImage.*;
import static com.betting.ground.auction.domain.QTag.*;
import static com.betting.ground.auction.domain.QView.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StopWatch;

import com.betting.ground.auction.dto.response.AuctionInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
class AuctionRepositoryImplTest {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	@Test
	void offset_pagenagtion() {
		// given

		String keyword = "1";
		String filter = "tag";
		Pageable pageable = PageRequest.of(3, 10);
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
		Long auctionId = 1999990L;
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
}
