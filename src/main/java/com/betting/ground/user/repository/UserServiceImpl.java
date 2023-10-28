package com.betting.ground.user.repository;

import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.user.domain.QUser;
import com.betting.ground.user.dto.UserNicknameDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;
    private UserRepository userRepository;

    //닉네임 갯수 조회
    @Transactional(readOnly = true)
    public int selectUserNickNameCount(String nickName) {
        return userRepository.countByNickname(nickName);
    }

    //닉네임 update
    @Transactional
    public void updateUserNickname(UserNicknameDTO userNicknameDTO) {
        try {
            jpaQueryFactory.update(QUser.user)
                    .set(QUser.user.nickname, userNicknameDTO.getNickname())
                    .where(QUser.user.email.eq(userNicknameDTO.getEmail()))
                    .execute();
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.BAD_REQUEST);
        }
    }

}
