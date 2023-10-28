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
import com.betting.ground.RefreshToken.domain.RefreshToken;
import com.betting.ground.RefreshToken.repository.RefreshTokenRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.config.jwt.JwtUtils;
import com.betting.ground.user.dto.LoginResponseDto;
import com.betting.ground.user.dto.ReissueRequestDto;
import com.betting.ground.user.dto.login.KakaoProfile;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.dto.login.OAuthToken;
import com.betting.ground.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.UUID;
import static com.betting.ground.common.exception.ErrorCode.EXPIRED_REFRESH_TOKEN;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * user정보 조회(email)
     */
    @Transactional(readOnly = true)
    public UserDTO selectUserProfileByEmail(String email) {
        Optional<User> userProfile = Optional.ofNullable(userRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(ErrorCode.BAD_REQUEST)));

        return userProfile.map(user ->
                UserDTO.builder()
                        .nickname(user.getNickname())
                        .profileImage(user.getProfileImage())
                        .bankName(user.getBankInfo() != null ? user.getBankInfo().getBankName() : null)
                        .bankAccount(user.getBankInfo() != null ? user.getBankInfo().getBankAccount() : null)
                        .roadName(user.getAddress() != null ? user.getAddress().getRoadName() : null)
                        .addressName(user.getAddress() != null ? user.getAddress().getAddressName() : null)
                        .zipcode(user.getAddress() != null ? user.getAddress().getZipcode() : null)
                        .detailAddress(user.getAddress() != null ? user.getAddress().getDetailAddress() : null)
                        .money(user.getMoney())
                        .email(user.getEmail())
                        .role(String.valueOf(user.getRole()))
                        .build()
        ).orElse(null);
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
//    @Modifying
//    @Transactional
//    public boolean updateUserNickName(UserNicknameDTO userNicknameDTO) {
//        //닉네임 중복 확인
//        if(selectUserNickNameCount(userNicknameDTO.getNickname()) > 0) {
//            return false;
//        }
//
//        //닉네임 수정후 update
//        jpaQueryFactory.update(QUser.user)
//                .set(QUser.user.nickname, userNicknameDTO.getNickname())
//                .where(QUser.user.email.eq(userNicknameDTO.getEmail()))
//                .execute();
//        return true;
//    }

    public boolean updateUserNickName(Long userId, UserNicknameDTO userNicknameDTO) {
        //닉네임 중복 확인
        userRepository.findByNickname(userNicknameDTO.getNickname()).ifPresent(
                a -> new GlobalException(ErrorCode.DUPLICATED_NICKNAME)
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.BAD_REQUEST)
        );

        user.updateNickname(userNicknameDTO.getNickname());
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
//    @Modifying
//    @Transactional
//    public void updateUserAddress(UserAddressDTO userAddressDTO) {
//        jpaQueryFactory.update(QUser.user)
//                .set(QUser.user.address.addressName, userAddressDTO.getAddressName())
//                .set(QUser.user.address.detailAddress, userAddressDTO.getDetailAddress())
//                .set(QUser.user.address.roadName, userAddressDTO.getRoadName())
//                .set(QUser.user.address.zipcode, userAddressDTO.getZipcode())
//                .where(QUser.user.email.eq(userAddressDTO.getEmail()))
//                .execute();
//    }

    public UserAddressDTO updateUserAddress(Long userId, UserAddressDTO userAddressDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        user.updateAddress(userAddressDTO);

        return userAddressDTO;
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

    public LoginResponseDto login(String code) throws JsonProcessingException {
        // 토큰 받아오기
        RestTemplate tokenRt = new RestTemplate();
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("client_id", "962fb2b8640dcff588a7cf43ac11a64b");
        tokenParams.add("redirect_uri", "http://localhost:8080/api/user/login/kakao");
        tokenParams.add("code", code);
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, tokenHeaders);
        ResponseEntity<String> response = tokenRt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                String.class
        );
        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        // 카카오 유저 정보 받아오기
        RestTemplate profileRt = new RestTemplate();
        MultiValueMap<String, String> profileParams = new LinkedMultiValueMap<>();
        profileParams.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(profileParams);
        ResponseEntity<String> infoResponse = profileRt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileRequest,
                String.class
        );
        KakaoProfile kakaoProfile = objectMapper.readValue(infoResponse.getBody(), KakaoProfile.class);

        // 회원 가입 됐는지 확인
        User user = null;
        if (!userRepository.existsByEmail(kakaoProfile.getKakao_account().getEmail())) {
            // 회원 가입
            user = User.builder()
                    .email(kakaoProfile.getKakao_account().getEmail())
                    .password(passwordEncoder.encode("password"))
                    .role(Role.GUEST)
                    .username(kakaoProfile.getKakao_account().getName())
                    .nickname(kakaoProfile.getKakao_account().getProfile().getNickname() + "(" + kakaoProfile.getId() + ")")
                    .profileImage(kakaoProfile.getKakao_account().getProfile().getProfile_image_url())
                    .phoneNumber(kakaoProfile.getKakao_account().getPhone_number())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);
        }

        // 로그인
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoProfile.getKakao_account().getEmail(), "password"));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(loginUser);

        RefreshToken refreshToken = RefreshToken.builder()
                .loginUser(loginUser)
                .refreshToken(UUID.randomUUID().toString())
                .build();
        refreshTokenRepository.save(refreshToken);

        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .userId(loginUser.getUser().getId())
                .email(loginUser.getUser().getEmail())
                .username(loginUser.getUser().getUsername())
                .nickname(loginUser.getUser().getNickname())
                .role(loginUser.getUser().getRole().toString())
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();

        return loginResponse;
    }

    public LoginResponseDto reissue(ReissueRequestDto request) {
        String refreshToken = request.getRefreshToken();

        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GlobalException(EXPIRED_REFRESH_TOKEN));
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(findRefreshToken.getLoginUser());

        return LoginResponseDto.builder()
                .userId(findRefreshToken.getLoginUser().getUser().getId())
                .email(findRefreshToken.getLoginUser().getUser().getEmail())
                .username(findRefreshToken.getLoginUser().getUser().getUsername())
                .nickname(findRefreshToken.getLoginUser().getUser().getNickname())
                .role(findRefreshToken.getLoginUser().getUser().getRole().toString())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
