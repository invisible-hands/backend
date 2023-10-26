package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.AuctionStatus;
import com.betting.ground.auction.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom{

}
