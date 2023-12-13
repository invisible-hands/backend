package com.betting.ground.auction.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.domain.AuctionImage;
import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.BidHistory;
import com.betting.ground.auction.domain.Tag;
import com.betting.ground.auction.domain.View;
import com.betting.ground.auction.dto.BidHistoryDto;
import com.betting.ground.auction.dto.BidInfo;
import com.betting.ground.auction.dto.CreateAuctionDto;
import com.betting.ground.auction.dto.SellerInfo;
import com.betting.ground.auction.dto.SellerItemDto;
import com.betting.ground.auction.dto.request.PayRequest;
import com.betting.ground.auction.dto.response.AuctionInfo;
import com.betting.ground.auction.dto.response.BidInfoResponse;
import com.betting.ground.auction.dto.response.ItemDetailDto;
import com.betting.ground.auction.dto.response.ItemResponse;
import com.betting.ground.auction.repository.AuctionImageRepository;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.auction.repository.BidHistoryRepository;
import com.betting.ground.auction.repository.TagRepository;
import com.betting.ground.auction.repository.ViewCacheRepository;
import com.betting.ground.auction.repository.ViewRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.common.s3.S3Utils;
import com.betting.ground.deal.domain.Deal;
import com.betting.ground.deal.domain.DealEvent;
import com.betting.ground.deal.repository.DealEventRepository;
import com.betting.ground.deal.repository.DealRepository;
import com.betting.ground.user.domain.Payment;
import com.betting.ground.user.domain.PaymentType;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.repository.PaymentRepository;
import com.betting.ground.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

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
	private final PaymentRepository paymentRepository;
	private final S3Utils s3Utils;

	@Transactional(readOnly = true)
	public ItemResponse getItems(Pageable pageable, Boolean progressFilter) {
		PageImpl<AuctionInfo> auctionInfo = auctionRepository.findItems(pageable, progressFilter);
		return new ItemResponse(auctionInfo);
	}

	@Transactional(readOnly = true)
	public ItemResponse search(String keyword, Pageable pageable) {
		PageImpl<AuctionInfo> auctionInfo = auctionRepository.findItemByKeywordByOrderByCreatedAtDesc(keyword,
			pageable);
		return new ItemResponse(auctionInfo);
	}

	@Transactional(noRollbackFor = {GlobalException.class})
	public BidInfoResponse getBidInfo(Long auctionId, Long userId) {
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(
			() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND, "존재 하지 않는 경매글입니다.")
		);

		if (auction.getEndAuctionTime().isBefore(LocalDateTime.now()) && auction.getAuctionStatus()
			.equals(AuctionStatus.AUCTION_PROGRESS)) {
			// 시간 지나고 status 업데이트 안 된 상태에서 get요청 들어오면 status 업데이트
			if (auction.getCurrentPrice() != null)
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

		User user = userRepository.findById(userId).orElseThrow(
			() -> new GlobalException(ErrorCode.USER_NOT_FOUND)
		);

		AuctionImage auctionImage = auctionImageRepository.findFirstByAuctionId(auctionId).orElseGet(() -> null);

		return new BidInfoResponse(auctionImage, auction, user);
	}

	public ItemDetailDto getItemDetail(LoginUser loginUser, Long auctionId, String uuid) {
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(
			() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
		);

		if (viewCacheRepository.addUniqueUUID(auctionId.toString(), uuid)) {
			viewCacheRepository.setAuction(auctionId.toString());
			viewCacheRepository.setUUID(auctionId.toString(), uuid);
		}

		auctionRepository.save(auction);
		return auctionRepository.findDetailAuctionById(loginUser, auctionId);
	}

	public void create(Long userId, CreateAuctionDto createAuctionDto) throws IOException {
		// 경매글을 작성할 유저가 존재하는지 확인
		User user = userRepository.findById(userId).orElseThrow(
			() -> new GlobalException(ErrorCode.USER_NOT_FOUND)
		);

		// 경매글 저장
		Auction auction = createAuctionDto.toEntity(user);
		auctionRepository.save(auction);

		//경매 사진 저장
		List<MultipartFile> images = createAuctionDto.getImages();
		if (images != null) {
			for (MultipartFile image : images) {
				String s3Url = s3Utils.upload(image);
				auctionImageRepository.save(new AuctionImage(s3Url, auction));
			}
		} else {
			throw new GlobalException(ErrorCode.NEED_IMAGES);
		}

		// 경매 태그 저장
		List<String> tags = createAuctionDto.getTags();
		if (tags != null) {
			for (String tagName : tags) {
				tagRepository.save(new Tag(tagName, auction));
			}
		}

		// 조회수 저장
		viewRepository.save(new View(auction.getId()));
	}

	public void delete(Long userId, Long auctionId) {
		// 해당 경매글 찾기
		Auction auction = auctionRepository.findById(auctionId).orElseThrow(
			() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
		);

		// 본인이 작성한 경매글이 아니면 삭제 불가
		if (!auction.getUser().getId().equals(userId)) {
			throw new GlobalException(ErrorCode.CAN_NOT_DELETE);
		}

		// 경매글 생성 5분 후 삭제 불가
		if (auction.getCreatedAt().plusMinutes(5L).isBefore(LocalDateTime.now())) {
			throw new GlobalException(ErrorCode.ALREADY_AUCTION_START);
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

		Auction findAuction = auctionRepository.findById(auctionId).orElseThrow(
			() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
		);

		// 판매자가 판매중인 물건 찾기
		PageImpl<SellerItemDto> findSellerItem = auctionRepository.findSellerItemBySellerId(
			findAuction.getUser().getId(), pageable);

		return new SellerInfo(findAuction.getUser(), findSellerItem);
	}

	public void instantBuy(Long auctionId, Long userId) {
		LocalDateTime now = LocalDateTime.now();

		// 비관적 락 select for update
		Auction auction = auctionRepository.findByLockId(auctionId).orElseThrow(
			() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
		);

		// 내가 올린 경매는 구매 불가
		if (auction.getUser().getId().equals(userId)) {
			throw new GlobalException(ErrorCode.CAN_NOT_PURCHASE);
		}

		// 경매 시간 만료시 예외
		if (auction.getEndAuctionTime().isBefore(now)) {
			throw new GlobalException(ErrorCode.AUCTION_TIME_OUT);
		}

		// 경매가 종료된 이후 요청이 들어올 경우 예외
		if (!auction.getAuctionStatus().equals(AuctionStatus.AUCTION_PROGRESS)) {
			throw new GlobalException(ErrorCode.AUCTION_SOLD_OUT);
		}

		// 경매 시작 후 5분 이내에 즉결 시도할 경우 예외
		if (auction.getCreatedAt().plusMinutes(5L).isAfter(now)) {
			throw new GlobalException(ErrorCode.AUCTION_NOT_START);
		}

		// 내 가상머니 차감
		User user = userRepository.findById(userId).orElseThrow(
			() -> new GlobalException(ErrorCode.USER_NOT_FOUND)
		);
		user.pay(auction.getInstantPrice());
		userRepository.save(user);

		// 즉시구매한 사람의 입출금내역
		Payment payment = new Payment(auctionId, auction.getInstantPrice(), PaymentType.OUT_BID, now, user);
		paymentRepository.save(payment);

		// 구매자 입찰내역
		BidHistory buyerBidHistory = new BidHistory(user.getId(), user.getNickname(), now, auction.getInstantPrice(),
			auction);
		bidHistoryRepository.save(buyerBidHistory);

		// 최근 입찰한 사람 찾아서 취소
		Optional<BidHistory> bidHistory = bidHistoryRepository.findByAuctionAndPrice(auction,
			auction.getCurrentPrice());
		if (bidHistory.isPresent()) {
			User bidder = userRepository.findById(bidHistory.get().getBidderId()).orElseThrow(
				() -> new GlobalException(ErrorCode.BAD_REQUEST)
			);
			bidder.bidCancel(bidHistory.get().getPrice());
			userRepository.save(bidder);
			Payment bidderPayment = new Payment(auctionId, bidHistory.get().getPrice(), PaymentType.IN_CANCEL, now,
				bidder);
			paymentRepository.save(bidderPayment);
		}

		// 경매 즉시거래가로 업데이트
		auction.updateBid(user.getId(), auction.getInstantPrice(), AuctionStatus.AUCTION_SUCCESS);
		auctionRepository.save(auction);

		Deal deal = new Deal(auction, now);
		dealRepository.save(deal);
	}

	public void bid(Long auctionId, PayRequest request, Long userId) {
		LocalDateTime now = LocalDateTime.now();

		// 비관적 락 select for update
		Auction auction = auctionRepository.findByLockId(auctionId).orElseThrow(
			() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND)
		);

		// 내가 올린 경매는 구매 불가
		if (auction.getUser().getId().equals(userId)) {
			throw new GlobalException(ErrorCode.CAN_NOT_PURCHASE);
		}

		// 경매 시간 만료시 예외
		if (auction.getEndAuctionTime().isBefore(now)) {
			throw new GlobalException(ErrorCode.AUCTION_TIME_OUT);
		}

		// 경매가 종료된 이후 요청이 들어올 경우 예외
		if (!auction.getAuctionStatus().equals(AuctionStatus.AUCTION_PROGRESS)) {
			throw new GlobalException(ErrorCode.AUCTION_SOLD_OUT);
		}

		// 경매 시작 후 5분 이내에 즉결 시도할 경우 예외
		if (auction.getCreatedAt().plusMinutes(5L).isAfter(now)) {
			throw new GlobalException(ErrorCode.AUCTION_NOT_START);
		}

		// 즉시 구매가격 이상 입찰 요청시 예외
		if (auction.getInstantPrice() <= request.getPrice()) {
			throw new GlobalException(ErrorCode.EXCEED_INSTANT_PRICE);
		}

		// 이미 최고가로 입찰한 입찰자가 재입찰한 경우 예외
		if (auction.getBidderId() != null && auction.getBidderId().equals(userId)) {
			throw new GlobalException(ErrorCode.ALREADY_TOP_PRICE_BIDDER);
		}

		// 현재 입찰가보다 낮은 가격으로 요청시 예외
		if (auction.getBidderId() != null && auction.getCurrentPrice() >= request.getPrice()
			|| auction.getCurrentPrice() > request.getPrice()) {
			throw new GlobalException(ErrorCode.LESS_THEN_CURRENT_PRICE);
		}

		// 내 가상머니 차감
		User user = userRepository.findById(userId).orElseThrow(
			() -> new GlobalException(ErrorCode.USER_NOT_FOUND)
		);
		user.pay(request.getPrice());
		userRepository.save(user);

		// 입찰한 사람의 입출금내역
		Payment payment = new Payment(auctionId, request.getPrice(), PaymentType.OUT_BID, now, user);
		paymentRepository.save(payment);

		// 구매자 입찰내역
		BidHistory buyerBidHistory = new BidHistory(user.getId(), user.getNickname(), now, request.getPrice(), auction);
		bidHistoryRepository.save(buyerBidHistory);

		// 최근 입찰한 사람 찾아서 취소
		Optional<BidHistory> bidHistory = bidHistoryRepository.findByAuctionAndPrice(auction,
			auction.getCurrentPrice());
		if (bidHistory.isPresent()) {
			User bidder = userRepository.findById(bidHistory.get().getBidderId()).orElseThrow(
				() -> new GlobalException(ErrorCode.BAD_REQUEST)
			);
			bidder.bidCancel(bidHistory.get().getPrice());
			userRepository.save(bidder);
			Payment bidderPayment = new Payment(auctionId, bidHistory.get().getPrice(), PaymentType.IN_CANCEL, now,
				bidder);
			paymentRepository.save(bidderPayment);
		}

		// 경매 현재 거래가로 업데이트
		auction.updateBid(user.getId(), request.getPrice(), AuctionStatus.AUCTION_PROGRESS);
		auctionRepository.save(auction);

	}
}
