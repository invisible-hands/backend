package com.betting.ground.auction.service;


import com.betting.ground.auction.domain.*;
import com.betting.ground.auction.dto.BidHistoryDto;
import com.betting.ground.auction.dto.BidInfo;
import com.betting.ground.auction.dto.SellerItemDto;
import com.betting.ground.auction.dto.SellerInfo;
import com.betting.ground.auction.dto.request.AuctionCreateRequest;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.auction.dto.response.ItemResponse;
import com.betting.ground.auction.repository.*;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.common.s3.S3Utils;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionImageRepository auctionImageRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ViewCacheRepository viewCacheRepository;
    private final ViewRepository viewRepository;

    private final BidHistoryRepository bidHistoryRepository;
    private final DealRepository dealRepository;
    private final DealEventRepository dealEventRepository;
    private final S3Utils s3Utils;

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

    public ItemDetailDto getItemDetail(LoginUser loginUser, Long auctionId, String uuid) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
        );

        if(viewCacheRepository.addUniqueUUID(auctionId.toString(), uuid)){
            viewCacheRepository.setAuction(auctionId.toString());
            viewCacheRepository.setUUID(auctionId.toString(), uuid);
        }

        auctionRepository.save(auction);
        return auctionRepository.findDetailAuctionById(loginUser, auctionId);
    }

    public void create(LoginUser loginUser, AuctionCreateRequest request, List<MultipartFile> images) throws IOException {
        // 경매글 작성자 찾기
        User user = userRepository.findById(loginUser.getUser().getId()).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        // 경매 저장
        Auction auction = new Auction(user, request);
        auction.calcEndAuctionTime(Duration.valueOf(request.getDuration()).getTime());
        auctionRepository.save(auction);

        //경매 사진 저장
        for (MultipartFile image : images ) {
            String s3Url = s3Utils.upload(image);
            auctionImageRepository.save(new AuctionImage(s3Url, auction));
        }

        // 태그 저장
        List<String> tags = request.getTags();
        for (String tagName : tags) {
            Tag auctionTag = new Tag(tagName, auction);
            tagRepository.save(auctionTag);
        }

        // 조회수 저장
        viewRepository.save(new View(auction.getId()));
    }

    public void delete(Long auctionId) {
        // 해당 경매글 찾기
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
        );

        // 경매글 생성 5분 후 삭제 불가
        if(auction.getCreatedAt().plusMinutes(5L).isBefore(LocalDateTime.now())) {
            new GlobalException(ErrorCode.ALREADY_AUCTION_START);
        }

        // 경매글 soft delete, 사진과 태그는 삭제
        auctionRepository.deleteById(auctionId);
        auctionImageRepository.deleteByAuctionId(auctionId);
        tagRepository.deleteByAcutionId(auctionId);
    }

    public BidHistoryDto getBidHistory(Long auctionId, Pageable pageable) {
        PageImpl<BidInfo> bidInfos = bidHistoryRepository.findBidInfoByAuctionId(auctionId, pageable);

        return new BidHistoryDto(bidInfos);
    }

    public SellerInfo getSeller(Long auctionId, Pageable pageable) {

        // 해당 경매글의 판매자 찾기
        User findSeller = auctionRepository.findSellerById(auctionId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        // 판매자가 판매중인 물건 찾기
        PageImpl<SellerItemDto> findSellerItem = auctionRepository.findSellerItemBySellerId(findSeller.getId(), pageable);

        return new SellerInfo(findSeller, findSellerItem);
    }
}
