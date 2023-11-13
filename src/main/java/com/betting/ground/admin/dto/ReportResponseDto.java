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
@NoArgsConstructor
public class ReportResponseDto {
    private Long id;

    @Schema(description = "신고 이유", example = "불법거래")
    private String reportReason;

    @Schema(description = "신고 설명", example = "영희라는 유저가 마약을 싸게 팔고 있습니다.")
    private String reportDescription;

    @Schema(description = "신고 상태", example = "PROGRESS")
    private ReportStatus reportStatus;

    //entity -> dto
    public static ReportResponseDto entityToDTO(Report report) {
        if(report == null) throw new GlobalException(ErrorCode.NOT_REPORT);

        return ReportResponseDto.builder()
                .id(report.getId())
                .reportReason(report.getReportReason())
                .reportDescription(report.getReportDescription())
                .reportStatus(report.getReportStatus())
                .build();
    }
}
