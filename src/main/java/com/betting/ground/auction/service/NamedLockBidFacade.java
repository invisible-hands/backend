package com.betting.ground.auction.service;

import org.springframework.stereotype.Component;

import com.betting.ground.auction.dto.request.PayRequest;
import com.betting.ground.auction.repository.LockRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NamedLockBidFacade {
	private final LockRepository lockRepository;
	private final AuctionService auctionService;

	public void bid(Long auctionId, PayRequest request, Long userId) {
		try {
			lockRepository.getLock(auctionId.toString());
			auctionService.bid(auctionId, request, userId);
		} finally {
			lockRepository.releaseLock(auctionId.toString());
		}
	}
}
