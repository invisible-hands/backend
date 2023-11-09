package com.betting.ground.admin.dto;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.domain.ReportStatus;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResponseDTO {
    private Long id;

    @Schema(description = "신고 이유", example = "불법거래")
    private String reportReason;

    @Schema(description = "신고 설명", example = "영희라는 유저가 마약을 싸게 팔고 있습니다.")
    private String reportDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "신고 시간", example = "2023-11-01")
    private LocalDateTime reportTime;

    @Schema(description = "신고 상태", example = "PROGRESS")
    private ReportStatus reportStatus;

    @Schema(description = "경매글 Id", example = "300")
    private Long auctionId;

    @Schema(description = "판매자 Id", example = "300")
    private Long userId;


}
