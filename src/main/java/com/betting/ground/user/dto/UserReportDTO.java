package com.betting.ground.user.dto;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.domain.ReportStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDTO {

    @NotBlank(message = "신고하시는 이유를 작성해 주세요.")
    @Schema(description = "신고 이유", example = "거래금지품목")
    private String reportReason;

    @NotBlank(message = "상황을 설명해 주세요.")
    @Schema(description = "신고 설명", example = "김경매라는 유저가 마약을 팔고 있습니다.")
    private String reportDescription;

    @NotNull(message = "경매글 id")
    @Schema(description = "auctionId", example = "1")
    private Long auctionId;

    //dto -> entity
    public static Report toReportEntity(UserReportDTO userReportDTO) {
        return Report.builder()
                .reportReason(userReportDTO.getReportReason())
                .reportDescription(userReportDTO.getReportDescription())
                .reportStatus(ReportStatus.PROGRESS)
                .auctionId(userReportDTO.getAuctionId())
                .build();
    }

}
