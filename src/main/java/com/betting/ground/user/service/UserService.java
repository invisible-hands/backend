package com.betting.ground.user.service;

import com.betting.ground.user.domain.QUser;
import com.betting.ground.user.domain.Role;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import com.betting.ground.user.dto.UserDTO;
import com.betting.ground.user.dto.UserNicknameDTO;
import com.betting.ground.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * user정보 조회(email)
     */
    @Transactional(readOnly = true)
    public User selectUserProfileByEmail(String email) {
        Optional<User> userProfile = userRepository.findByEmail(email);
        return userProfile.orElse(null);
    }

    /**
     * 해당 nickName 갯수 조회
     */
    @Transactional(readOnly = true)
    public int selectUserNickNameCount(String nickName) {
        return userRepository.countByNickname(nickName);
    }

    /**
     * 닉네임 수정
     */
    @Modifying
    @Transactional
    public boolean updateUserNickName(UserNicknameDTO userNicknameDTO) {
        //닉네임 중복 확인
        if(selectUserNickNameCount(userNicknameDTO.getNickname()) > 0) {
            return false;
        }

        //닉네임 수정후 update
        jpaQueryFactory.update(QUser.user)
                .set(QUser.user.nickname, userNicknameDTO.getNickname())
                .where(QUser.user.email.eq(userNicknameDTO.getEmail()))
                .execute();
        return true;
    }

    /**
     * 계좌 번호 등록 및 수정
     */
    @Modifying
    @Transactional
    public void updateUserAccount(UserAccountDTO userAccountDTO) {
        jpaQueryFactory.update(QUser.user)
                .set(QUser.user.bankInfo.bankAccount, userAccountDTO.getBankAccount())
                .set(QUser.user.bankInfo.bankName, userAccountDTO.getBankName())
                .where(QUser.user.email.eq(userAccountDTO.getEmail()))
                .execute();
    }

    /**
     * 주소 등록 및 수정
     */
    @Modifying
    @Transactional
    public void updateUserAddress(UserAddressDTO userAddressDTO) {
        jpaQueryFactory.update(QUser.user)
                .set(QUser.user.address.addressName, userAddressDTO.getAddressName())
                .set(QUser.user.address.detailAddress, userAddressDTO.getDetailAddress())
                .set(QUser.user.address.roadName, userAddressDTO.getRoadName())
                .set(QUser.user.address.zipcode, userAddressDTO.getZipcode())
                .where(QUser.user.email.eq(userAddressDTO.getEmail()))
                .execute();
    }

    /**
     * 권한 변경
     */
    @Modifying
    @Transactional
    public void updateUserRole(UserDTO userDTO) {
        jpaQueryFactory.update(QUser.user)
                .set(QUser.user.role, Role.USER)
                .where(QUser.user.email.eq(userDTO.getEmail()))
                .execute();
    }
}
