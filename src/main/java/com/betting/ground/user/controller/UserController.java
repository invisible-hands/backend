package com.betting.ground.user.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import com.betting.ground.user.dto.UserDTO;
import com.betting.ground.user.dto.UserNicknameDTO;
import com.betting.ground.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "유저", description = "유저 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/userProfile/{email}")
    @Operation(summary = "유저 프로필 조회")
    public Response<?> getProfile(@PathVariable String email) {
        log.info("입력값 : {}", email);
        //조회한 정보를 반환([예시] - 추후 db에서 반환)
        UserDTO userDTO = UserDTO.builder()
                .email(email)
                .build();
        //결과 리턴
        if(email != null)
            return Response.success("유저 프로필 조회 완료", userDTO);
        else
            return Response.error("401", "해당 유저는 없는 유저입니다.");
    }

    @PostMapping("/nicknameUpdate")
    @Operation(summary = "닉네임 수정")
    public Response<?> editNickname(@RequestBody @Valid UserNicknameDTO userNicknameDTO, BindingResult bindingResult) {
        log.info("입력값 : {}", userNicknameDTO);

        //1. email검증
        if(ObjectUtils.isEmpty(userNicknameDTO.getEmail())) {
            //유효성 검증 에러 추가
            bindingResult.addError(new FieldError("UserNicknameDTO"
                    , "email"
                    , "이메일을 입력하셔야 됩니다."));
        }

        //2. 닉네임 유효성 검증
        String nicknameRegex = "^[a-zA-Z0-9가-힣]*$"; // 닉네임에는 영어,숫자,한글만 사용가능
        if(!userNicknameDTO.getNickname().matches(nicknameRegex)) {
            bindingResult.addError(new FieldError("UserNicknameDTO"
                    , "nickname"
                    , "닉네임은 영어, 숫자, 한글만 포함할 수 있습니다."));
        }

        //3. 에러가 존재하면 error리턴
        if(bindingResult.hasErrors()) return Response.error("500", bindingResult.getFieldError().getDefaultMessage());

        //결과 리턴
        return Response.success("닉네임 수정 완료", userNicknameDTO);
    }

    @PostMapping("/account")
    @Operation(summary = "계좌 번호 등록")
    public Response<?> registerAccountNumber(@RequestBody @Valid UserAccountDTO userAccountDTO, BindingResult bindingResult) {
        log.info("입력값 : {}", userAccountDTO);
        //1. 이메일 검증
        if(ObjectUtils.isEmpty(userAccountDTO.getEmail())) {
            //유효성 검증 에러 추가
            bindingResult.addError(new FieldError("UserAccountDTO"
                    , "email"
                    , "이메일을 입력하셔야 됩니다."));
            return Response.error("401", bindingResult.getFieldError("email").getDefaultMessage());
        }

        //2. 에러가 존재하면 error리턴
        if(bindingResult.hasErrors()) return Response.error("500", "시스템 에러");

        //결과 리턴
        return Response.success("계좌 번호 등록 완료", userAccountDTO);
    }

    @PostMapping("/accountUpdate")
    @Operation(summary = "계좌 번호 수정")
    public Response<?> editAccountNumber(@RequestBody @Valid UserAccountDTO userAccountDTO, BindingResult bindingResult) {
        log.info("입력값 : {}", userAccountDTO);

        //1. 이메일 검증
        if(ObjectUtils.isEmpty(userAccountDTO.getEmail())) {
            //유효성 검증 에러 추가
            bindingResult.addError(new FieldError("UserAccountDTO"
                    , "email"
                    , "이메일을 입력하셔야 됩니다."));
            return Response.error("401", bindingResult.getFieldError("email").getDefaultMessage());
        }

        //2. 에러가 존재하면 error리턴
        if(bindingResult.hasErrors()) return Response.error("500","시스템 에러");

        //결과리턴
        return Response.success("계좌 번호 수정 완료", userAccountDTO);
    }

    @PostMapping("/address")
    @Operation(summary = "주소 등록")
    public Response<?> registerAddress(@RequestBody @Valid UserAddressDTO userAddressDTO, BindingResult bindingResult) {
        log.info("입력값 : {}", userAddressDTO);
        //1. 이메일 검증
        if(ObjectUtils.isEmpty(userAddressDTO.getEmail())) {
            //유효성 검증 에러 추가
            bindingResult.addError(new FieldError("UserAddressDTO"
                    , "email"
                    , "이메일을 입력하셔야 됩니다."));
            return Response.error("401", bindingResult.getFieldError("email").getDefaultMessage());
        }

        //2. 에러가 존재하면 error리턴
        if(bindingResult.hasErrors()) return Response.error("500","시스템 에러(db 연결 불가)");

        //결과리턴
        return Response.success("주소 등록 완료", userAddressDTO);
    }

    @PostMapping("/addressUpdate")
    @Operation(summary = "주소 수정")
    public Response<?> editAddress(@RequestBody @Valid UserAddressDTO userAddressDTO, BindingResult bindingResult) {
        log.info("입력값 : {}", userAddressDTO);
        //1. 이메일 검증
        if(ObjectUtils.isEmpty(userAddressDTO.getEmail())) {
            //유효성 검증 에러 추가
            bindingResult.addError(new FieldError("UserAddressDTO"
                    , "email"
                    , "이메일을 입력하셔야 됩니다."));
            return Response.error("401", bindingResult.getFieldError("email").getDefaultMessage());
        }

        //2. 에러가 존재하면 error리턴
        if(bindingResult.hasErrors()) return Response.error("500","시스템 에러(db 연결 불가)");

        //결과 리턴
        return Response.success("주소 수정 완료", userAddressDTO);
    }

    @PostMapping("/userRoleUpdate")
    @Operation(summary = "활성 유저 전환")
    public Response<?> userRoleUpdate(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        log.info("입력값 : {}", userDTO);
        //1. 이메일 검증
        if(ObjectUtils.isEmpty(userDTO.getEmail())) {
            //유효성 검증 에러 추가
            bindingResult.addError(new FieldError("UserDTO"
                    , "email"
                    , "이메일을 입력하셔야 됩니다."));
            return Response.error("401", bindingResult.getFieldError("email").getDefaultMessage());
        }

        //2. 에러가 존재하면 error리턴
        if(bindingResult.hasErrors()) return Response.error("500","시스템 에러(db 연결 불가)");

        //3. user의 role이 guest일 경우엔 전환
        if(userDTO.getRole().equals("guest")) return Response.success("활성 유저 전환 완료", userDTO);

        //결과 리턴
        return Response.error("402","이미 전환된 유저입니다.");
    }

}

