package com.betting.ground.auction.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.Duration;
import com.betting.ground.auction.domain.ItemCondition;
import com.betting.ground.auction.domain.View;
import com.betting.ground.auction.service.AuctionService;
import com.betting.ground.common.scheduler.SchedulerController;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.repository.UserRepository;

@SpringBootTest
class ViewCacheRepositoryTest {

	@Autowired
	private ViewCacheRepository viewCacheRepository;
	@Autowired
	private ViewRepository viewRepository;
	@Autowired
	private SchedulerController schedulerController;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuctionRepository auctionRepository;
	@Autowired
	private AuctionService auctionService;

	@Test
	void 레디스_테스트() throws Exception {
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

		Auction auction = Auction.builder()
			.title("aaa")
			.content("내용")
			.itemCondition(ItemCondition.NEW)
			.startPrice(1000L)
			.instantPrice(30000L)
			.duration(Duration.HALF)
			.user(seller)
			.build();
		auctionRepository.save(auction);

		View view = new View(auction.getId());
		viewRepository.save(view);

		//given
		String uuid = UUID.randomUUID().toString();
		auctionService.getItemDetail(new LoginUser(seller), auction.getId(), uuid);
		Thread.sleep(3000);
		auctionService.getItemDetail(new LoginUser(seller), auction.getId(), uuid);

		//when
		//        for(int i=0; i<10; i++){
		//            if (i % 3 == 0) {
		//                auctionService.getItemDetail(new LoginUser(seller), auction.getId(), uuid);
		//            } else {
		//                auctionService.getItemDetail(new LoginUser(seller), auction.getId(), UUID.randomUUID().toString());
		//            }
		//        }
		schedulerController.migration();

		Thread.sleep(5000);

		auctionService.getItemDetail(new LoginUser(seller), auction.getId(), uuid);
		schedulerController.migration();
		//then
	}

	@Test
	void 삭제() throws Exception {
		//given
		//        viewCacheRepository.removeAllUUID();
		//        viewCacheRepository.removeAllAuctions();
		//        System.out.println(viewCacheRepository.getAllAuctions());
		System.out.println(viewCacheRepository.getAllAuctions().isEmpty());
		//when

		//then
	}

}
