package com.betting.ground.deal.repository;

import com.betting.ground.deal.domain.DealEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealEventRepository extends JpaRepository<DealEvent, Long> {
}
