package com.betting.ground.admin.repository;

import com.betting.ground.admin.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    //userID로 조회
    Optional<List<Report>> findAllByUserId(Long userId);
}