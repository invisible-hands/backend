package com.betting.ground.admin.service;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.dto.ReportRequestDTO;
import com.betting.ground.admin.dto.ReportResponseDTO;
import com.betting.ground.admin.repository.ReportRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.dto.login.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final ReportRepository reportRepository;

    //신고내역 전체 조회(게시판)
    @Transactional(readOnly = true)
    public List<ReportResponseDTO> searchReport() {
        List<Report> reports = reportRepository.findAll();

        return reports.stream()
                .map(this::entityToDTO)
                .toList();
    }

    //신고 내용확인
    @Transactional(readOnly = true)
    public ReportResponseDTO detailReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.BAD_REQUEST));

        return entityToDTO(report);
    }

    //신고 완료 처리
    public ReportResponseDTO reportStatusUpdate(ReportRequestDTO reportRequestDTO) {
        Report report = reportRepository.findById(reportRequestDTO.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.BAD_REQUEST));

        //변경 감지
        report.updateReportStatus();

        return entityToDTO(report);
    }

    //entity -> dto 반환
    public ReportResponseDTO entityToDTO(Report report) {
        if(report == null) throw new GlobalException(ErrorCode.NOT_REPORT);

        return ReportResponseDTO.builder()
                .id(report.getId())
                .reportReason(report.getReportReason())
                .reportDescription(report.getReportDescription())
                .reportTime(report.getReportTime())
                .reportStatus(report.getReportStatus())
                .auctionId(report.getAuctionId())
                .userId(report.getUserId())
                .build();
    }

    //관리자 권한체크
    public boolean roleCheck(LoginUser loginUser) {
        List<String> authorityValues = loginUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        for(String auth : authorityValues) {
            if(auth.equals("ROLE_" + Role.ADMIN)) {
                return false;
            }
        }
        return true;
    }

}
