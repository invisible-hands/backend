package com.betting.ground.auction.service;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.auction.dto.response.ItemResponse;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final DealRepository dealRepository;
    private final DealEventRepository dealEventRepository;

    @Transactional(readOnly = true)
    public ItemResponse getNewItem(Pageable pageable) {
        PageImpl<AuctionInfo> auctionInfo = auctionRepository.findItemByOrderByCreatedAtDesc(pageable);
        return new ItemResponse(auctionInfo);
    }

    @Transactional(readOnly = true)
    public ItemResponse getDeadline(Pageable pageable) {
        PageImpl<AuctionInfo> auctionInfo = auctionRepository.findItemByOrderByEndAuctionTimeAsc(pageable);
        return new ItemResponse(auctionInfo);
    }

    @Transactional(readOnly = true)
    public ItemResponse getMostView(Pageable pageable) {
        PageImpl<AuctionInfo> auctionInfo = auctionRepository.findItemByOrderByViewCntDesc(pageable);
        return new ItemResponse(auctionInfo);
    }

    @Transactional(readOnly = true)
    public ItemResponse search(String keyword, Pageable pageable) {
        PageImpl<AuctionInfo> auctionInfo = auctionRepository.findItemByKeywordByOrderByCreatedAtDesc(keyword, pageable);
        return new ItemResponse(auctionInfo);
    }

    @Transactional(noRollbackFor = {GlobalException.class})
    public BidInfoResponse getBidInfo(Long auctionId, Long userId){
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND, "존재 하지 않는 경매글입니다.")
        );

        if(auction.getEndAuctionTime().isBefore(LocalDateTime.now()) && auction.getAuctionStatus().equals(AuctionStatus.AUCTION_PROGRESS)){
            // 시간 지나고 status 업데이트 안 된 상태에서 get요청 들어오면 status 업데이트
            if(auction.getCurrentPrice() != null)
                auction.updateAuctionStatus(AuctionStatus.AUCTION_SUCCESS);
            else
                auction.updateAuctionStatus(AuctionStatus.AUCTION_FAIL);

            Deal deal = new Deal(auction);
            dealRepository.save(deal);
            DealEvent dealEvent = new DealEvent(deal);
            dealEventRepository.save(dealEvent);

            // 경매 종료시간 지났을 경우 예외
            throw new GlobalException(ErrorCode.AUCTION_TIME_OUT, "만료된 경매입니다.");
        }

        return auctionRepository.getBidInfo(auctionId, userId);
    }

    public ItemDetailDto getItemDetail(Long userId, Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new GlobalException(ErrorCode.BAD_REQUEST)
        );
        auction.updateViewCnt();
        auctionRepository.save(auction);
        return auctionRepository.findDetailAuctionById(userId, auctionId);
    }
}
