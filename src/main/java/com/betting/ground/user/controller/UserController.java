package com.betting.ground.user.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.user.domain.User;
import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import com.betting.ground.user.dto.UserDTO;
import com.betting.ground.user.dto.UserNicknameDTO;
import com.betting.ground.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping //토큰값으로 유저 정보를 가져올수있음, UserID로 유저 정보를 뿌림.
    @Operation(summary = "유저 프로필 조회")
    public Response<?> getProfile(@RequestParam String email) {
        //유저정보 가져오기(추후 카카오 로그인 브랜치와 머지시 주석 해제)
        //현재는 머지하기 전이라 파라미터로 해당 유저 프로필 조회.
        //String email = tokenGetEmail();
        log.info("입력값 : {}", email);

        User user = userService.selectUserProfileByEmail(email);

        //결과 리턴
        if(user != null) return Response.success("조회 완료되었습니다.", user);

        return Response.success("해당 유저는 없는 유저입니다.", null);
    }

    @PutMapping("/nickname")
    @Operation(summary = "닉네임 등록,수정")
    public Response<?> editNickname(
            @RequestBody @Valid @Parameter(description = "유저 닉네임 정보") UserNicknameDTO userNicknameDTO,
            BindingResult bindingResult)
    {
        log.info("입력값 : {}", userNicknameDTO);
        String message = "닉네임 수정 완료되었습니다.";

        //1. 유효성 검증
        if(bindingResult.hasErrors()) {
            Map<String, String> errorMessages = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.put(error.getField(), error.getDefaultMessage());
            }
            return Response.success("다시 입력해주세요.", errorMessages);
        }
        //2. db에서 유저 이름을 찾아서 업데이트
        if(!userService.updateUserNickName(userNicknameDTO)) message = "이미 사용중인 닉네임입니다.";

        //결과 리턴
        return Response.success(message, null);
    }


    @PutMapping("/account")
    @Operation(summary = "계좌 번호 등록,수정")
    public Response<?> editAccountNumber(
            @RequestBody @Valid @Parameter(description = "유저 계좌 정보") UserAccountDTO userAccountDTO,
            BindingResult bindingResult)
    {
        log.info("입력값 : {}", userAccountDTO);

        //1. 유효성 검증
        if(bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return Response.success("다시 입력해주세요.", errorMessages);
        }

        //2. db에서 유저 이름을 찾아서 업데이트
        userService.updateUserAccount(userAccountDTO);

        //결과리턴
        return Response.success("처리 완료 되었습니다.", userAccountDTO);
    }


    @PutMapping("/address")
    @Operation(summary = "주소 등록,수정")
    public Response<?> editAddress(
            @RequestBody @Valid @Parameter(description = "유저 주소 정보") UserAddressDTO userAddressDTO,
            BindingResult bindingResult)
    {
        log.info("입력값 : {}", userAddressDTO);
        //1. 유효성 검증
        if(bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return Response.success("다시 입력해주세요.", errorMessages);
        }

        //2. db에서 유저 이름을 찾아서 업데이트
        userService.updateUserAddress(userAddressDTO);

        //결과 리턴
        return Response.success("처리 완료 되었습니다.", userAddressDTO);
    }

    @PutMapping("/role")
    @Operation(summary = "활성 유저 전환")
    public Response<?> editRole(
            @RequestBody @Valid @Parameter(description = "유저 정보") UserDTO userDTO,
            BindingResult bindingResult)
    {
        log.info("입력값 : {}", userDTO);
        //1. 유효성 검증
        if(bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            return Response.success("다시 입력해주세요.", errorMessages);
        }

        //2. db에서 유저 이름을 찾아서 업데이트
        userService.updateUserRole(userDTO);

        //3. user의 role이 guest일 경우엔 전환
        return Response.success("활성 유저 전환 완료", null);
    }

    /**
     * 이메일 추출
     */
    private String tokenGetEmail() {
        //로그인 인증정보 체크(카카오 로그인 참고)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return null;
        }
        //결과 반환
        //LoginUser loginUser = (LoginUser) auth.getPrincipal();
        //return loginUser.getUser().getEmail();
        return null;
    }

}