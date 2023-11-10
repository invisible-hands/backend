package com.betting.ground.admin.dto;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.domain.ReportStatus;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResponseDTO {
    private Long id;

    @Schema(description = "신고 이유", example = "거래금지품목")
    private String reportReason;

    @Schema(description = "신고 설명", example = "진우라는 유저가 마약을 팔고 있습니다.")
    private String reportDescription;

    @Schema(description = "신고 상태", example = "PROGRESS")
    private ReportStatus reportStatus;

    @Schema(description = "경매글 Id", example = "12")
    private Long auctionId;

    public static ReportResponseDTO from(Report report) {
        if (report == null) {
            throw new GlobalException(ErrorCode.NOT_REPORT);
        }
        return ReportResponseDTO.builder()
                .id(report.getId())
                .reportReason(report.getReportReason())
                .reportDescription(report.getReportDescription())
                .auctionId(report.getAuctionId())
                .reportStatus(report.getReportStatus())
                .build();
    }

}
