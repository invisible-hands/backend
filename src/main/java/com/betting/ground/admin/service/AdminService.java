package com.betting.ground.admin.service;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.dto.ReportRequestDto;
import com.betting.ground.admin.dto.ReportResponseDto;
import com.betting.ground.admin.dto.ReportResponseDtoList;
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
    public ReportResponseDtoList searchReport() {
        List<Report> reports = reportRepository.findAll();

        List<ReportResponseDto> reportResponseDTOS = reports.stream()
                .map(ReportResponseDto::entityToDTO)
                .toList();

        return ReportResponseDtoList.builder()
                .reportResDTOS(reportResponseDTOS)
                .build();
    }

    //신고 내용확인
    @Transactional(readOnly = true)
    public ReportResponseDto detailReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.BAD_REQUEST));

        return ReportResponseDto.entityToDTO(report);
    }

    //신고 완료 처리
    public ReportResponseDto reportStatusUpdate(ReportRequestDto reportRequestDTO) {
        Report report = reportRepository.findById(reportRequestDTO.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.BAD_REQUEST));

        //변경 감지
        report.updateReportStatus();

        return ReportResponseDto.entityToDTO(report);
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
