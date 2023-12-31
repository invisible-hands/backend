package com.betting.ground.user.service;

import com.betting.ground.RefreshToken.domain.RefreshToken;
import com.betting.ground.RefreshToken.repository.RefreshTokenRepository;
import com.betting.ground.auction.domain.Auction;
import com.betting.ground.auction.repository.AuctionRepository;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.config.jwt.JwtUtils;
import com.betting.ground.user.domain.Role; 
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.*;
import com.betting.ground.user.dto.login.KakaoProfile;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.dto.login.OAuthToken;
import com.betting.ground.admin.repository.ReportRepository; 
import com.betting.ground.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import static com.betting.ground.common.exception.ErrorCode.*; 
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
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

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final ReportRepository reportRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Value("${kakao.login.password}")
    private String password;

    //유저 프로필 조회
    @Transactional(readOnly = true)
    public UserDto selectUserProfileById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.BAD_REQUEST));

        return new UserDto(user);
    }

    //닉네임 변경
    public UserNicknameDto updateUserNickName(Long userId, UserNicknameDto userNicknameDTO) {
        //닉네임 중복 확인
        userRepository.findByNickname(userNicknameDTO.getNickname()).ifPresent(
                a -> {
                    throw new GlobalException(ErrorCode.DUPLICATED_NICKNAME);
                }
        );

        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.BAD_REQUEST)
        );
        user.updateNickname(userNicknameDTO.getNickname());
        return userNicknameDTO;
    }


    //계좌 번호 등록 및 수정
    public UserAccountDto updateUserAccount(Long userId, UserAccountDto userAccountDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        user.updateBank(userAccountDTO);
        return userAccountDTO;
    }

    //주소 등록 및 수정
    public UserAddressDto updateUserAddress(Long userId, UserAddressDto userAddressDTO) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new GlobalException(ErrorCode.USER_NOT_FOUND)
        );

        user.updateAddress(userAddressDTO);

        return userAddressDTO;
    }

    //권한 변경
    public LoginResponseDto updateUserRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        if(user.getRole().equals(Role.USER)) {
            throw new GlobalException(ErrorCode.USER_ALREADY_ACTIVE);
        }

        if (user.getBankInfo() == null || user.getAddress() == null) {
            throw new GlobalException(ErrorCode.NOT_ENOUGH_INFO);
        }

        user.updateRole();

        LoginUser loginUser = new LoginUser(user);
        // 엑세스 토큰 생성
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(loginUser);

        // 리프레시 토큰 생성
        RefreshToken refreshToken = new RefreshToken(loginUser, UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);

        return new LoginResponseDto(user, accessToken, refreshToken.getRefreshToken());
    }

    //게시글 신고하기
    public UserReportDto saveUserReport(Long userId, Long auctionId, UserReportDto userReportDTO) {
        //경매글 조회
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.AUCTION_NOT_FOUND));

        //신고자 정보 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        //유저정보 조회
        reportRepository.save(UserReportDto.toReportEntity(userReportDTO, auction, user));
        return userReportDTO;
    }


    public LoginResponseDto login(String code) throws JsonProcessingException {
        // 토큰 받아오기
        OAuthToken oAuthToken = getOAuthToken(code);

        // 카카오 유저 정보 받아오기
        KakaoProfile kakaoProfile = getKakaoProfile(oAuthToken);

        // 회원 가입 됐는지 확인
        if (!userRepository.existsByEmail(kakaoProfile.getKakao_account().getEmail())) {
            // 회원 가입
            userRepository.save(new User(kakaoProfile, passwordEncoder.encode(password)));
        }

        // 로그인
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoProfile.getKakao_account().getEmail(), password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 엑세스 토큰 생성
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(loginUser);

        // 리프레시 토큰 생성
        RefreshToken refreshToken = new RefreshToken(loginUser, UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);

        return new LoginResponseDto(loginUser.getUser(), accessToken, refreshToken.getRefreshToken());
    }

    private OAuthToken getOAuthToken(String code) throws JsonProcessingException {
        RestTemplate tokenRt = new RestTemplate();
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128)));
//        tokenRt.setRequestFactory(factory);

        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("client_id", "962fb2b8640dcff588a7cf43ac11a64b");
//                tokenParams.add("redirect_uri", "http://localhost:5173/redirection");
//        tokenParams.add("redirect_uri", "http://localhost:8080/api/user/login/kakao");
        tokenParams.add("redirect_uri", "http://localhost:8080/api/user/code");
//        tokenParams.add("redirect_uri", "http://ka1425de5708ea.user-app.krampoline.com/api/user/code");
        tokenParams.add("code", code);
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenParams, tokenHeaders);
        ResponseEntity<String> response = tokenRt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                tokenRequest,
                String.class
        );
        return objectMapper.readValue(response.getBody(), OAuthToken.class);
    }

    private KakaoProfile getKakaoProfile(OAuthToken oAuthToken) throws JsonProcessingException {

        RestTemplate profileRt = new RestTemplate();
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("krmp-proxy.9rum.cc", 3128)));
//        profileRt.setRequestFactory(factory);

        MultiValueMap<String, String> profileParams = new LinkedMultiValueMap<>();
        profileParams.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        HttpEntity<MultiValueMap<String, String>> profileRequest = new HttpEntity<>(profileParams);
        ResponseEntity<String> infoResponse = profileRt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                profileRequest,
                String.class
        );
        return objectMapper.readValue(infoResponse.getBody(), KakaoProfile.class);
    }

    public LoginResponseDto reissue(ReissueRequestDto request) {
        String refreshToken = request.getRefreshToken();

        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(
                () -> new GlobalException(EXPIRED_REFRESH_TOKEN));

        // 엑세스토큰 재발급
        String accessToken = jwtUtils.generateAccessTokenFromLoginUser(findRefreshToken.getLoginUser());

        return new LoginResponseDto(findRefreshToken.getLoginUser().getUser(), accessToken, refreshToken);
    }

}
