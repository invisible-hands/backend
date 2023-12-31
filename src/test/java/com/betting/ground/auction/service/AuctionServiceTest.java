package com.betting.ground.auction.service;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.Duration;
import com.betting.ground.auction.domain.ItemCondition;
import com.betting.ground.auction.dto.request.PayRequest;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import com.betting.ground.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Disabled
class AuctionServiceTest {

    @Autowired
    private AuctionService auctionService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void 레디스_조회수() throws Exception{
        //given
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

        for (int i = 0; i < 10; i++) {
            User buyer = User.builder()
                    .username("구매자" + i)
                    .nickname("11111111")
                    .email("aaaaaaaa")
                    .password("dsdf")
                    .profileImage("이미지")
                    .bankInfo(null)
                    .address(null)
                    .phoneNumber("전화번호")
                    .money(1000000L)
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(buyer);
        }


        Auction auction = Auction.builder()
                .title("aaa")
                .content("내용")
                .itemCondition(ItemCondition.NEW)
                .startPrice(1000L)
                .instantPrice(30000L)
                .currentPrice(0L)
                .auctionStatus(AuctionStatus.AUCTION_PROGRESS)
                .duration(Duration.HALF)
                .createdAt(LocalDateTime.now().minusHours(1L))
                .updatedAt(LocalDateTime.now())
                .user(seller)
                .build();
        auction.calcEndAuctionTime(Duration.HALF.getTime());
        auctionRepository.save(auction);


        LoginUser loginUser = new LoginUser(seller);


//        auctionService.instantBuy(1L, new PayRequest(30000L), 2L);

        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(30);

        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    long random = (int) (Math.random() * 10) + 2L;
                    auctionService.instantBuy(1L,  random);

                    success.getAndIncrement();
                } catch (GlobalException e) {
                    fail.getAndIncrement();
                } finally {
                    latch.countDown();
                    System.out.println("success : " + success);
                    System.out.println("fail : " + fail);
                }
            });
        }
        latch.await();

//        HashOperations<String, Long, Long> hashOperations = redisTemplate.opsForHash();
//        System.out.println(hashOperations.get("Auction", "1"));
//        Set keys = hashOperations.keys("Auction");
//        for (Object key : keys) {
//            System.out.println(Long.valueOf((String)key));
//        }
        //when

        //then
    }




    @Test
    public void bidTest() throws Exception {
        //given
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
                .currentPrice(0L)
                .auctionStatus(AuctionStatus.AUCTION_PROGRESS)
                .duration(Duration.HALF)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(seller)
                .build();
        auction.calcEndAuctionTime(Duration.HALF.getTime());
        auctionRepository.save(auction);

        for (int i = 0; i < 10; i++) {
            User buyer = User.builder()
                    .username("구매자" + i)
                    .nickname("11111111")
                    .email("aaaaaaaa")
                    .password("dsdf")
                    .profileImage("이미지")
                    .bankInfo(null)
                    .address(null)
                    .phoneNumber("전화번호")
                    .money(1000000L)
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(buyer);
        }

        auctionService.bid(1L, new PayRequest(2000L), 2L);
        auctionService.bid(1L, new PayRequest(3000L), 3L);

        //when

//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(30);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        AtomicInteger success = new AtomicInteger();
//        AtomicInteger fail = new AtomicInteger();
//        for (int i = 0; i < threadCount; i++) {
//            int finalI = i;
//            executorService.submit(() -> {
//                try {
//                    long random = (int) (Math.random() * 10) + 2L;
//                    System.out.println("random = " + random);
//                    auctionService.bid(1L, new PayRequest(2500L), random);
//                    success.getAndIncrement();
//                } catch (GlobalException e) {
//                    fail.getAndIncrement();
//                } finally {
//                    System.out.println("success : " + success);
//                    System.out.println("fail : " + fail);
//                    latch.countDown();
//                }
//            });
//        }
//        latch.await();

        //then

    }
}

