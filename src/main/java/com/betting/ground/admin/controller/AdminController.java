package com.betting.ground.admin.controller;

import com.betting.ground.admin.dto.ReportRequestDTO;
import com.betting.ground.admin.dto.ReportResponseDTO;
import com.betting.ground.admin.dto.ReportResponseDTOList;
import com.betting.ground.admin.service.AdminService;
import com.betting.ground.common.dto.Response;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.dto.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "관리자", description = "관리자 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/reportList")
    @Operation(summary = "신고 전체조회")
    public Response<ReportResponseDTOList> getReportList(@AuthenticationPrincipal LoginUser loginUser) {
        if(adminService.roleCheck(loginUser)) throw new GlobalException(ErrorCode.NOT_ADMIN_ROLE);

        return Response.success("신고내역 전체 조회 완료", adminService.searchReport());
    }

    @GetMapping("/report/{id}")
    @Operation(summary = "신고 상세조회")
    public Response<ReportResponseDTO> getReport(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginUser loginUser) {
        if(adminService.roleCheck(loginUser)) throw new GlobalException(ErrorCode.NOT_ADMIN_ROLE);

        return Response.success("해당 신고내역 조회 완료", adminService.detailReport(id));
    }

    @PutMapping("/reportComplete")
    @Operation(summary = "신고 처리완료")
    public Response<ReportResponseDTO> completeReport(
            @RequestBody @Valid @Parameter(description = "회원 신고 정보") ReportRequestDTO reportRequestDTO,
            @AuthenticationPrincipal LoginUser loginUser) {
        if(adminService.roleCheck(loginUser)) throw new GlobalException(ErrorCode.NOT_ADMIN_ROLE);

        return Response.success("신고처리 완료", adminService.reportStatusUpdate(reportRequestDTO));
    }
}
