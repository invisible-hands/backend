package com.betting.ground.user.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.user.dto.UserAccountDto;
import com.betting.ground.user.dto.UserAddressDto;
import com.betting.ground.user.dto.UserDto;
import com.betting.ground.user.dto.UserNicknameDto;
import com.betting.ground.user.dto.*;
import com.betting.ground.user.dto.login.LoginUser;
import com.betting.ground.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "유저", description = "유저 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 로그인")
    public Response<LoginResponseDto> kakaoLogin(
            @Parameter(description = "카카오에서 받은 code", example = "")
            @RequestParam String code) throws JsonProcessingException {

        return Response.success("카카오 로그인 성공", userService.login(code));
    }

    @Hidden
    @GetMapping("/code")
    public String code(String code) {
        return code;
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급")
    public Response<LoginResponseDto> reissue(
            @Valid @RequestBody ReissueRequestDto request) {
        return Response.success("토큰 재발행을 성공했습니다.", userService.reissue(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public Response<Void> logout(@AuthenticationPrincipal LoginUser loginUser) {
        String nickname = loginUser.getUser().getNickname();

        return Response.success(nickname + " 로그아웃 성공", null);
    }

    //프로필 관련 API
    @GetMapping
    @Operation(summary = "유저 프로필 조회")
    public Response<UserDto> getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        return Response.success("유저 프로필 조회 완료.", userService.selectUserProfileById(loginUser.getUser().getId()));
    }

    @PutMapping("/nickname")
    @Operation(summary = "닉네임 등록,수정")
    public Response<UserNicknameDto> editNickname(
            @RequestBody @Valid @Parameter(description = "유저 닉네임 정보") UserNicknameDto userNicknameDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        log.info("입력값 : {}", userNicknameDto);
        return Response.success("처리 완료 되었습니다.", userService.updateUserNickName(loginUser.getUser().getId(), userNicknameDto));
    }

    @PutMapping("/account")
    @Operation(summary = "계좌 번호 등록,수정")
    public Response<UserAccountDto> editAccountNumber(
            @RequestBody @Valid @Parameter(description = "유저 계좌 정보") UserAccountDto userAccountDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        log.info("입력값 : {}", userAccountDto);
        return Response.success("처리 완료 되었습니다.", userService.updateUserAccount(loginUser.getUser().getId(), userAccountDto));
    }

    @PutMapping("/address")
    @Operation(summary = "주소 등록,수정")
    public Response<UserAddressDto> editAddress(
            @RequestBody @Valid @Parameter(description = "유저 주소 정보") UserAddressDto userAddressDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        log.info("입력값 : {}", userAddressDto);
        return Response.success("주소 변경 완료.", userService.updateUserAddress(loginUser.getUser().getId(), userAddressDto));
    }

    @PutMapping("/role")
    @Operation(summary = "활성 유저 전환 가입일은 (yyyy-MM-dd HH:mm:ss)로 요청해주세요")
    public Response<LoginResponseDto> editRole(
            @RequestBody @Valid @Parameter(description = "유저 정보") UserDto userDto,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        log.info("입력값 : {}", userDto);
        return Response.success("활성 유저 전환 완료", userService.updateUserRole(loginUser.getUser().getId()));
    }

    @PutMapping("/report")
    @Operation(summary = "회원신고 등록")
    public Response<UserReportDto> editReport(
            @RequestBody @Valid @Parameter(description = "회원 신고 정보") UserReportDto userReportDto
    ) {
        log.info("입력값 : {}", userReportDto);
        return Response.success("회원 신고 완료", userService.saveUserReport(userReportDto));
    }

}


