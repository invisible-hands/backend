package com.betting.ground.auction.service;

import com.betting.ground.auction.dto.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
    public ItemResponse getNewItem(Pageable pageable) {
        return null;
    }

    public ItemResponse getDeadline(Pageable pageable) {
        return null;
    }

    public ItemResponse getMostView(Pageable pageable) {
        return null;
    }
}
