package com.betting.ground.admin.dto;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.domain.ReportStatus;
import com.betting.ground.auction.domain.Auction;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTO {
    private Long id;

    @Schema(description = "신고 이유", example = "불법거래")
    private String reportReason;

    @Schema(description = "신고 설명", example = "영희라는 유저가 마약을 싸게 팔고 있습니다.")
    private String reportDescription;

    @Schema(description = "신고 상태", example = "PROGRESS")
    private ReportStatus reportStatus;

    @Schema(description = "경매글정보", example = "300")
    private Auction auction;

    @Schema(description = "신고자정보", example = "300")
    private User user;

    //entity -> dto
    public static ReportResponseDTO entityToDTO(Report report) {
        if(report == null) throw new GlobalException(ErrorCode.NOT_REPORT);

        return ReportResponseDTO.builder()
                .id(report.getId())
                .reportReason(report.getReportReason())
                .reportDescription(report.getReportDescription())
                .reportStatus(report.getReportStatus())
                .auction(report.getAuction())
                .user(report.getUser())
                .build();
    }
}
