package com.betting.ground.common.scheduler;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class SchedulerController {
    private final AuctionRepository auctionRepository;
    private final DealRepository dealRepository;
    private final DealEventRepository dealEventRepository;

    @GetMapping
    @Transactional
    public void update() {
        List<Auction> auctions = auctionRepository.findAllByAuctionStatus(AuctionStatus.AUCTION_PROGRESS);
        List<DealEvent> dealEvents = auctions.stream()
                .filter(auction -> auction.getEndAuctionTime().isBefore(LocalDateTime.now()))
                .map(auction -> {
                    if (auction.getCurrentPrice() == null) {
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
}
