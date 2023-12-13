package com.betting.ground.admin.controller;

import com.betting.ground.admin.dto.ReportResponseDto;
import com.betting.ground.admin.dto.ReportResponseDtoList;
import com.betting.ground.admin.service.AdminService;
import com.betting.ground.common.dto.Response;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.dto.login.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "관리자", description = "관리자 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/report")
    @Operation(summary = "신고 전체조회")
    public Response<ReportResponseDtoList> getReportList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return Response.success("신고내역 전체 조회 완료", adminService.searchReport(page, size));
    }


    @GetMapping("/report/{reportId}")
    @Operation(summary = "신고 상세조회")
    public Response<ReportResponseDto> getReport(
            @PathVariable Long reportId) {

        return Response.success("해당 신고내역 조회 완료", adminService.detailReport(reportId));
    }

    @PutMapping("/report/{reportId}/complete")
    @Operation(summary = "신고 처리완료")
    public Response<Void> completeReport(
            @PathVariable long reportId) {
        adminService.reportStatusUpdate(reportId);

        return Response.success("신고처리 완료", null);
    }

    @PostMapping("/report/user/{userId}")
    @Operation(summary = "해당 유저 신고목록 조회")
    public Response<ReportResponseDtoList> userReports(
            @PathVariable long userId) {

        return Response.success("신고목록 조회 완료", adminService.userReports(userId));
    }
}
