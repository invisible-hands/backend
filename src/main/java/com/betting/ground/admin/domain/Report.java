package com.betting.ground.admin.domain;

import jakarta.persistence.*;
import lombok.*;

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
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    private Long auctionId;

    public void updateReportStatus() {
        this.reportStatus = ReportStatus.COMPLETE;
    }
}
