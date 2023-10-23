package com.betting.ground.auction.domain;

import com.betting.ground.common.entity.BaseTimeEntity;
import com.betting.ground.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE auction SET is_Deleted = true WHERE id = ?")
@Where(clause = "is_Deleted = false")
public class Auction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private ItemCondition itemCondition;
    private Long startPrice;
    @Enumerated(EnumType.STRING)
    private Duration duration;
    private Long instantPrice;
    private int viewCnt;
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
