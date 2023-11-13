package com.betting.ground.admin.repository;

import com.betting.ground.admin.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> searchReportById(Long id);
}