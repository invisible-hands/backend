package com.betting.ground.auction.service;

import com.betting.ground.auction.dto.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionService {
    public ItemResponse getNewItem() {
        return null;
    }

    public ItemResponse getDeadline() {
        return null;
    }

    public ItemResponse getMostView() {
        return null;
    }
}
