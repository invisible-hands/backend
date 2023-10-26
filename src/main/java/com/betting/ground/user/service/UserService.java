package com.betting.ground.user.service;

import com.betting.ground.auction.repository.ItemRepository;
import com.betting.ground.user.dto.response.PurchaseInfoResponse;
import com.betting.ground.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public PurchaseInfoResponse getAllPurchases(Long userId, Pageable pageable){
        return new PurchaseInfoResponse(itemRepository.getAllPurchases(userId, pageable));
    }

    public PurchaseInfoResponse getBeforeShippingPurchases(Long userId, Pageable pageable) {
        return new PurchaseInfoResponse(itemRepository.getBeforePurchases(userId, pageable));
    }
}
