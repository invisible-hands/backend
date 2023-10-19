package com.betting.ground.user.domain;

import com.betting.ground.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET isDeleted = true WHERE id = ?")
@Where(clause = "isDeleted = false")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nickname;
    private String email;
    @Embedded
    private Bank bankInfo;
    @Embedded
    private Address address;
    private String phoneNumber;
    private Long money;
    private boolean isDeleted;
    @Enumerated(EnumType.STRING)
    private Role role;

}
