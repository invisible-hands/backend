package com.betting.ground;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionImage;
import com.betting.ground.auction.domain.Duration;
import com.betting.ground.auction.domain.ItemCondition;
import com.betting.ground.auction.domain.Tag;
import com.betting.ground.auction.domain.View;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.repository.UserRepository;

@SpringBootTest
public class Data {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionRepository auctionRepository;

	@Test
	void test() {
		// given
		User seller = User.builder()
			.username("판매자")
			.nickname("ㅁㅁㅁ")
			.email("aaa")
			.password("dsdf")
			.profileImage("이미지")
			.bankInfo(null)
			.address(null)
			.phoneNumber("전화번호")
			.money(10000L)
			.role(Role.USER)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		userRepository.save(seller);

		List<Auction> auctions = new ArrayList<>();

		List<View> views = new ArrayList<>();
		for (int i = 1000001; i <= 2000000; i++) {
			Auction auction = Auction.builder()
				.title(i + "Title")
				.content("content")
				.itemCondition(ItemCondition.NEW)
				.startPrice(1000L)
				.instantPrice(30000L)
				.duration(Duration.HALF)
				.user(seller)
				.createdAt(LocalDateTime.now().plusSeconds(i))
				.build();
			auctions.add(auction);
			View view = new View((long)i, i);
			views.add(view);
		}
		bulkInsertAuctions(auctions, 1000);
		bulkInsertViews(views, 1000);

	}

	public void bulkInsertViews(List<View> views, int batchSize) {
		String sql = "INSERT INTO view (auction_id, cnt) VALUES (:auctionId, :cnt)";
		int totalSize = views.size();
		for (int i = 0; i < totalSize; i += batchSize) {
			List<View> batchList = views.subList(i, Math.min(i + batchSize, totalSize));
			SqlParameterSource[] batch = batchList.stream()
				.map(view -> new MapSqlParameterSource()
					.addValue("auctionId", view.getAuctionId())
					.addValue("cnt", view.getCnt())
				)
				.toArray(SqlParameterSource[]::new);
			jdbcTemplate.batchUpdate(sql, batch);
		}
	}

	public void bulkInsertAuctions(List<Auction> auctions, int batchSize) {
		String sql = "INSERT INTO auction (title, content, item_condition, start_price, instant_price, " +
			"bidder_id, current_price, auction_status, duration, end_auction_time, created_at, updated_at, is_deleted, user_id) "
			+
			"VALUES (:title, :content, :itemCondition, :startPrice, :instantPrice, " +
			":bidderId, :currentPrice, :auctionStatus, :duration, :endAuctionTime, :createdAt, :updatedAt, :isDeleted, :userId)";
		int totalSize = auctions.size();
		for (int i = 0; i < totalSize; i += batchSize) {
			List<Auction> batchList = auctions.subList(i, Math.min(i + batchSize, totalSize));
			SqlParameterSource[] batch = batchList.stream()
				.map(auction -> new MapSqlParameterSource()
					.addValue("title", auction.getTitle())
					.addValue("content", auction.getContent())
					.addValue("itemCondition", auction.getItemCondition().toString())
					.addValue("startPrice", auction.getStartPrice())
					.addValue("instantPrice", auction.getInstantPrice())
					.addValue("bidderId", auction.getBidderId())
					.addValue("currentPrice", auction.getCurrentPrice())
					.addValue("auctionStatus", auction.getAuctionStatus().toString())
					.addValue("duration", auction.getDuration().toString())
					.addValue("endAuctionTime", auction.getEndAuctionTime())
					.addValue("createdAt", auction.getCreatedAt())
					.addValue("updatedAt", auction.getUpdatedAt())
					.addValue("isDeleted", auction.isDeleted())
					.addValue("userId", auction.getUser().getId())
				)
				.toArray(SqlParameterSource[]::new);

			jdbcTemplate.batchUpdate(sql, batch);
		}
	}

	@Test
	void test2() {
		int pageNum = 0;
		Page<Auction> page;

		do {
			Pageable pageable = PageRequest.of(pageNum, 10000);
			page = auctionRepository.findAll(pageable);
			List<Auction> auctions = page.getContent();

			List<AuctionImage> auctionImages = new ArrayList<>();
			List<Tag> tags = new ArrayList<>();
			for (int i = 0; i < auctions.size(); i++) {
				AuctionImage auctionImage = new AuctionImage("aa" + i, auctions.get(i));
				AuctionImage auctionImage2 = new AuctionImage("bb" + i, auctions.get(i));
				auctionImages.add(auctionImage);
				auctionImages.add(auctionImage2);

				Tag tag = new Tag(i + "tag", auctions.get(i));
				Tag tag2 = new Tag(i + "tag2", auctions.get(i));
				tags.add(tag);
				tags.add(tag2);
			}

			bulkInsertAuctionImages(auctionImages, 1000);
			bulkInsertTag(tags, 1000);

			pageNum++;
		} while (page.hasNext());

	}

	public void bulkInsertAuctionImages(List<AuctionImage> auctionImages, int batchSize) {
		String sql = "INSERT INTO auction_image (image_url, thumbnail_url, auction_id) VALUES (:imageUrl, :thumbnailUrl, :auctionId)";
		int totalSize = auctionImages.size();
		for (int i = 0; i < totalSize; i += batchSize) {
			List<AuctionImage> batchList = auctionImages.subList(i, Math.min(i + batchSize, totalSize));
			SqlParameterSource[] batch = batchList.stream()
				.map(auctionImage -> new MapSqlParameterSource()
					.addValue("imageUrl", auctionImage.getImageUrl())
					.addValue("thumbnailUrl", auctionImage.getThumbnailUrl())
					.addValue("auctionId", auctionImage.getAuction().getId())
				)
				.toArray(SqlParameterSource[]::new);
			jdbcTemplate.batchUpdate(sql, batch);
		}
	}

	public void bulkInsertTag(List<Tag> tags, int batchSize) {
		String sql = "INSERT INTO tag (tag_name, auction_id) VALUES(:tagName, :auctionId)";
		int totalSize = tags.size();
		for (int i = 0; i < totalSize; i += batchSize) {
			List<Tag> batchList = tags.subList(i, Math.min(i + batchSize, totalSize));
			SqlParameterSource[] batch = batchList.stream()
				.map(tag -> new MapSqlParameterSource()
					.addValue("tagName", tag.getTagName())
					.addValue("auctionId", tag.getAuction().getId())
				)
				.toArray(SqlParameterSource[]::new);
			jdbcTemplate.batchUpdate(sql, batch);
		}
	}

}
