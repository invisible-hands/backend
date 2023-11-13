package com.betting.ground.common.scheduler;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.View;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.auction.repository.ViewCacheRepository;
import com.betting.ground.auction.repository.ViewRepository;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.domain.DealStatus;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Slf4j
@Hidden
public class SchedulerController {
    private final AuctionRepository auctionRepository;
    private final DealRepository dealRepository;
    private final DealEventRepository dealEventRepository;
    private final ViewRepository viewRepository;
    private final ViewCacheRepository viewCacheRepository;

    @GetMapping("/auction")
    @Transactional
    public void update() {
        log.info("status update");
        List<Auction> auctions = auctionRepository.findAllByAuctionStatus(AuctionStatus.AUCTION_PROGRESS);
        List<DealEvent> dealEvents = auctions.stream()
                .filter(auction -> auction.getEndAuctionTime().isBefore(LocalDateTime.now()))
                .map(auction -> {
                    if (!auction.hasBidder()) {
                        auction.updateAuctionStatus(AuctionStatus.AUCTION_FAIL);
                    } else {
                        auction.updateAuctionStatus(AuctionStatus.AUCTION_SUCCESS);
                    }
                    Deal deal = dealRepository.save(new Deal(auction));
                    return new DealEvent(deal);
                })
                .toList();
        dealEventRepository.saveAll(dealEvents);
        auctionRepository.saveAll(auctions);
    }

    @GetMapping("/deal")
    @Transactional
    public void update2() {
        log.info("deal");
        List<Deal> deals = dealRepository.findAllByDealStatus(DealStatus.DELIVERY_WAITING);
        deals.stream()
                .filter(deal -> deal.getDealDeadLine() != null)
                .filter(deal -> deal.getDealDeadLine().isBefore(LocalDateTime.now()))
                .forEach(deal -> deal.updateStatus(DealStatus.PURCHASE_CANCEL));
        dealRepository.saveAll(deals);
    }

    @GetMapping("/migration")
    @Transactional
    public void migration() {
        log.info("migration");
        Set<String> allAuctions = viewCacheRepository.getAllAuctions();
        if (allAuctions.isEmpty()) {
            return;
        }

        List<Long> auctionIds = allAuctions.stream().map(Long::valueOf).toList();
        List<View> views = viewRepository.findAllByAuctionIdIn(auctionIds);
        for (View view : views) {
            view.updateCount(viewCacheRepository.getViewCount(view.getAuctionId().toString()));
        }
        viewCacheRepository.removeAllUUID();
        viewCacheRepository.removeAllAuctions();
    }
}
