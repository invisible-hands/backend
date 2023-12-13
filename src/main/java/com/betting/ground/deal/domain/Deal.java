package com.betting.ground.deal.domain;

import java.time.LocalDateTime;

import com.betting.ground.auction.domain.Auction;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long sellerId;
	private Long buyerId;
	private Long dealPrice;
	private LocalDateTime dealTime;
	private LocalDateTime dealDeadLine;
	@Enumerated(EnumType.STRING)
	private DealStatus dealStatus;

	@OneToOne(fetch = FetchType.LAZY)
	private Auction auction;

	public void updateStatus(DealStatus status) {
		this.dealStatus = status;
	}

	public Deal(Auction auction) {
		this.sellerId = auction.getUser().getId();
		this.buyerId = auction.getBidderId();
		this.dealPrice = auction.getCurrentPrice();
		this.dealTime = auction.getEndAuctionTime();
		this.dealStatus = auction.getBidderId() == null ? DealStatus.SALE_FAIL : DealStatus.DELIVERY_WAITING;
		this.dealDeadLine = auction.getBidderId() == null ? null : auction.getEndAuctionTime().plusHours(48L);
		this.auction = auction;
	}

	public Deal(Auction auction, LocalDateTime now) {
		this.sellerId = auction.getUser().getId();
		this.buyerId = auction.getBidderId();
		this.dealPrice = auction.getCurrentPrice();
		this.dealTime = now;
		this.dealStatus = DealStatus.DELIVERY_WAITING;
		this.dealDeadLine = auction.getBidderId() == null ? null : auction.getEndAuctionTime().plusHours(48L);
		this.auction = auction;
	}
}
