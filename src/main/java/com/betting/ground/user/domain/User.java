package com.betting.ground.user.domain;

import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.dto.UserAccountDto;
import com.betting.ground.user.dto.UserAddressDto;
import com.betting.ground.user.dto.login.UserLoginRequestDto;
import com.betting.ground.user.dto.login.UserSignUpRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
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

    public void updateBank(UserAccountDto userAccountDTO) {
        this.bankInfo = new Bank(userAccountDTO);
    }

    public void updateAddress(UserAddressDto dto){
        this.address = new Address(dto);
    }

    public void updateRole() {
        this.role = Role.USER;
    }

    public void updateGuest() {
        this.role = Role.GUEST;
    }

    public void increaseMoney(Long money) {
        this.money += money;
    }

    public static User createUser(UserSignUpRequestDto userSignUpRequestDto) {
        return User.builder()
                .email(userSignUpRequestDto.getEmail())
                .username(userSignUpRequestDto.getUsername())
                .nickname(userSignUpRequestDto.getNickname())
                .password(userSignUpRequestDto.getPassword())
                .role(Role.USER)
                .build();
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

    public void settle(Long price) {
        this.money += price;
    }
}
