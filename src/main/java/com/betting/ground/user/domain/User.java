package com.betting.ground.user.domain;

import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nickname;
    private String email;
    private String password;
    private String profileImage;
    @Embedded
    private Bank bankInfo;
    @Embedded
    private Address address;
    private String phoneNumber;
    private Long money;
    @Enumerated(EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateBank(UserAccountDTO userAccountDTO) {
        this.bankInfo = new Bank(userAccountDTO);
    }

    public void updateAddress(UserAddressDTO dto){
        this.address = new Address(dto);
    }

    public void updateRole() {
        this.role = Role.USER;
    }

    public void increaseMoney(Long money) {
        this.money += money;
    }

    public User(KakaoProfile kakaoProfile, String password) {
        this.username = kakaoProfile.getKakao_account().getName();
        this.nickname = kakaoProfile.getKakao_account().getProfile().getNickname() + "(" + kakaoProfile.getId() + ")";
        this.email = kakaoProfile.getKakao_account().getEmail();
        this.password = password;
        this.profileImage = kakaoProfile.getKakao_account().getProfile().getProfile_image_url();
        this.phoneNumber = kakaoProfile.getKakao_account().getPhone_number();
        this.money = 0L;
        this.role = Role.GUEST;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void pay(Long price) {
        if (this.money < price) {
            throw new GlobalException(ErrorCode.NOT_ENOUGH_MONEY);
        }
        this.money -= price;
    }

    public void bidCancel(Long price) {
        this.money += price;
    }
}
