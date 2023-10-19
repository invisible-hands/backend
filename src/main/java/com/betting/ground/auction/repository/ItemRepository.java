package com.betting.ground.auction.repository;

import com.betting.ground.auction.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
