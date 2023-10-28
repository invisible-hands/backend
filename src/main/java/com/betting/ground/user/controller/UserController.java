package com.betting.ground.user.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import com.betting.ground.user.dto.UserDTO;
import com.betting.ground.user.dto.UserNicknameDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Hidden
    @GetMapping("/test1")
    public String test1() {
        return "test1";
    }

    @Hidden
    @GetMapping("/auth/test2")
    public String test2(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser.getUser().toString();
    }

    @Hidden
    @GetMapping("test3")
    public String test3(@AuthenticationPrincipal LoginUser loginUser) {
        return loginUser.getUser().toString();
    }

    //프로필 관련 API

    @GetMapping //
    @Operation(summary = "유저 프로필 조회")
    public Response<UserDTO> getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        return Response.success("유저 프로필 조회 완료.", userService.selectUserProfileByEmail(loginUser.getUser().getEmail()));
    }

    @PutMapping("/nickname")
    @Operation(summary = "닉네임 등록,수정")
    public Response<UserNicknameDTO> editNickname(
            @RequestBody @Valid @Parameter(description = "유저 닉네임 정보") UserNicknameDTO userNicknameDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        log.info("입력값 : {}", userNicknameDTO);
        String message = "닉네임 수정 완료되었습니다.";

        //db에서 유저 이름을 찾아서 업데이트
        if (!userService.updateUserNickName(userNicknameDTO)) message = "이미 사용중인 닉네임입니다.";
        return Response.success(message, userNicknameDTO);
    }


    @PutMapping("/account")
    @Operation(summary = "계좌 번호 등록,수정")
    public Response<UserAccountDTO> editAccountNumber(
            @RequestBody @Valid @Parameter(description = "유저 계좌 정보") UserAccountDTO userAccountDTO) {
        log.info("입력값 : {}", userAccountDTO);

        //db에서 유저 이름을 찾아서 업데이트
        userService.updateUserAccount(userAccountDTO);
        return Response.success("처리 완료 되었습니다.", userAccountDTO);
    }


//    @PutMapping("/address")
//    @Operation(summary = "주소 등록,수정")
//    public Response<UserAddressDTO> editAddress(
//            @RequestBody @Valid @Parameter(description = "유저 주소 정보") UserAddressDTO userAddressDTO) {
//        log.info("입력값 : {}", userAddressDTO);
//        //db에서 유저 이름을 찾아서 업데이트
//        userService.updateUserAddress(userAddressDTO);
//        return Response.success("처리 완료 되었습니다.", userAddressDTO);
//    }

    @PutMapping("/address")
    @Operation(summary = "주소 등록,수정")
    public Response<UserAddressDTO> editAddress(
            @RequestBody @Valid @Parameter(description = "유저 주소 정보") UserAddressDTO userAddressDTO,
            @AuthenticationPrincipal LoginUser loginUser
    ) {
        log.info("입력값 : {}", userAddressDTO);
        //db에서 유저 이름을 찾아서 업데이트

        return Response.success("주소 변경 완료.", userService.updateUserAddress(loginUser.getUser().getId(), userAddressDTO));
    }

    @PutMapping("/role")
    @Operation(summary = "활성 유저 전환")
    public Response<UserDTO> editRole(
            @RequestBody @Valid @Parameter(description = "유저 정보") UserDTO userDTO) {
        log.info("입력값 : {}", userDTO);

        //db에서 유저 이름을 찾아서 업데이트
        userService.updateUserRole(userDTO);
        return Response.success("활성 유저 전환 완료", userDTO);
    }
}