package com.betting.ground.admin.repository;

import com.betting.ground.admin.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {
}
