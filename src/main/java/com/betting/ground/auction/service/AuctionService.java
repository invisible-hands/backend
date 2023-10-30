package com.betting.ground.auction.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.betting.ground.auction.domain.*;
import com.betting.ground.auction.dto.BidHistoryDto;
import com.betting.ground.auction.dto.BidInfo;
import com.betting.ground.auction.dto.BiddingItemDto;
import com.betting.ground.auction.dto.SellerInfo;
import com.betting.ground.auction.dto.request.AuctionCreateRequest;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.auction.dto.response.ItemResponse;
import com.betting.ground.auction.repository.AuctionImageRepository;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.auction.repository.BidHistoryRepository;
import com.betting.ground.auction.repository.TagRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.config.s3.S3Config;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
    private final AuctionRepository auctionRepository;
    private final AuctionImageRepository auctionImageRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final S3Config s3Config;
    private final BidHistoryRepository bidHistoryRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private String localLocation;

    @PostConstruct
    public void init() {
        localLocation = System.getProperty("user.dir") + "/";
    }

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

    public ItemDetailDto getItemDetail(LoginUser loginUser, Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(
                () -> new GlobalException(ErrorCode.BAD_REQUEST)
        );
        auction.updateViewCnt();
        auctionRepository.save(auction);
        return auctionRepository.findDetailAuctionById(loginUser, auctionId);
    }

    public void create(LoginUser loginUser, AuctionCreateRequest request, List<MultipartFile> images) throws IOException {
        User user = userRepository.findById(loginUser.getUser().getId()).get();

        // 경매 저장
        Auction auction = Auction.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .itemCondition(ItemCondition.valueOf(request.getItemCondition()))
                .startPrice(request.getStartPrice())
                .instantPrice(request.getInstantPrice())
                .currentPrice(request.getStartPrice())
                .auctionStatus(AuctionStatus.AUCTION_PROGRESS)
                .duration(Duration.DAY)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build();

        auction.calcEndAuctionTime(Duration.valueOf(request.getDuration()).getTime());

        auctionRepository.save(auction);

        //경매 사진 저장
        for (MultipartFile image : images ) {
            String fileName = image.getOriginalFilename();
            String ext = fileName.substring(fileName.indexOf("."));

            String uuidFileName = UUID.randomUUID() + ext;
            String localPath = localLocation + uuidFileName;

            File localFile = new File(localPath);
            image.transferTo(localFile);

            s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
            String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

            localFile.delete();

            AuctionImage auctionImage = AuctionImage.builder()
                    .imageUrl(s3Url)
                    .auction(auction)
                    .build();

            auctionImageRepository.save(auctionImage);
        }

        // 태그 저장
        List<String> tags = request.getTags();
        for (String tag : tags) {
            Tag auctionTag = Tag.builder()
                    .tagName(tag)
                    .auction(auction)
                    .build();

            tagRepository.save(auctionTag);
        }
    }

    public void delete(Long auctionId) {
        auctionRepository.deleteById(auctionId);
        auctionImageRepository.deleteByAuctionId(auctionId);
        tagRepository.deleteByAcutionId(auctionId);
    }


    public BidHistoryDto getBidHistory(Long auctionId, Pageable pageable) {
        PageImpl<BidInfo> auctionInfo = bidHistoryRepository.findBidInfoByAuctionId(auctionId, pageable);

        return BidHistoryDto.builder()
                .bids(auctionInfo.getContent())
                .currentPage(auctionInfo.getNumber())
                .totalPage(auctionInfo.getTotalPages())
                .build();
    }

    public SellerInfo getSeller(Long auctionId, Pageable pageable) {

        User findSeller = auctionRepository.findSellerById(auctionId);
        PageImpl<BiddingItemDto> findBiddingItem = auctionRepository.findSellerItemBySellerId(findSeller.getId(), pageable);

        SellerInfo sellerInfo = SellerInfo.builder()
                .sellerId(findSeller.getId())
                .nickname(findSeller.getNickname())
                .profileImage(findSeller.getProfileImage())
                .auctionCnt(findBiddingItem.getTotalElements())
                .auctionList(findBiddingItem.getContent())
                .currentPage(findBiddingItem.getNumber())
                .totalPage(findBiddingItem.getTotalPages())
                .build();

        return sellerInfo;

    }
}
