package com.betting.ground.admin.domain;

import com.betting.ground.auction.domain.Auction;
import com.betting.ground.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reportReason;
    private String reportDescription;
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;
    @ManyToOne
    private Auction auction;
    @ManyToOne
    private User user;

    public void updateReportStatus() {
        this.reportStatus = ReportStatus.COMPLETE;
    }
}
