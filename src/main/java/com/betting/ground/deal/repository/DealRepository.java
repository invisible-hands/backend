package com.betting.ground.deal.repository;

import com.betting.ground.deal.domain.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
