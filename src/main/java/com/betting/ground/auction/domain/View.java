package com.betting.ground.auction.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "idx_auction_id", columnList = "auctionId"))
public class View {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long auctionId;

	private int cnt;

	public View(Long auctionId) {
		this.auctionId = auctionId;
		this.cnt = 0;
	}

	public void updateCount(int count) {
		this.cnt += count;
	}

	public View(Long auctionId, int cnt) {
		this.auctionId = auctionId;
		this.cnt = cnt;
	}
}
