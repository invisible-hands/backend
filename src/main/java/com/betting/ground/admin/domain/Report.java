package com.betting.ground.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reportReason;
    private String reportDescription;
    private LocalDateTime reportTime;
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    private Long auctionId;
    private Long userId;

    public void updateReportStatus() {
        this.reportStatus = ReportStatus.COMPLETE;
    }
}
