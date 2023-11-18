package com.betting.ground.admin.repository;

import com.betting.ground.admin.domain.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    //userID로 조회
    List<Report>findAllByUserId(Long userId);
    Page<Report> findAll(Pageable pageable);
}