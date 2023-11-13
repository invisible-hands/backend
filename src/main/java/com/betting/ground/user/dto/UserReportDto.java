package com.betting.ground.user.dto;

import com.betting.ground.admin.domain.Report;
import com.betting.ground.admin.domain.ReportStatus;
import com.betting.ground.auction.domain.Auction;
import com.betting.ground.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserReportDto {

    @NotBlank(message = "신고하시는 이유를 작성해 주세요.")
    @Schema(description = "신고 이유", example = "불법거래")
    private String reportReason;

    @NotBlank(message = "상황을 설명해 주세요.")
    @Schema(description = "신고 설명", example = "영희라는 유저가 마약을 싸게 팔고 있습니다.")
    private String reportDescription;

    @NotNull(message = "경매글 id")
    @Schema(description = "auctionId", example = "3")
    private Long auctionId;

    @NotNull(message = "신고자 id")
    @Schema(description = "userId", example = "3")
    private Long userId;

    //dto -> entity
    public static Report toReportEntity(UserReportDto userReportDTO, Auction auction, User user) {
        return Report.builder()
                .reportReason(userReportDTO.getReportReason())
                .reportDescription(userReportDTO.getReportDescription())
                .reportStatus(ReportStatus.PROGRESS)
                .auction(auction)
                .user(user)
                .build();
    }

}
