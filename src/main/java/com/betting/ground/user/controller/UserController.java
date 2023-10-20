package com.betting.ground.user.controller;
import com.betting.ground.common.dto.Response;
import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import com.betting.ground.user.dto.UserDTO;
import com.betting.ground.user.dto.UserNameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
//import org.springframework.http.HttpStatus;
//import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    @GetMapping
    @Operation(summary = "유저 프로필 조회")
    public Response<UserDTO> getProfile() {
        return Response.success("유저 프로필 조회 완료", new UserDTO());
    }

    @PutMapping("/nickname")
    @Operation(summary = "닉네임 수정")
    public Response<UserNameDTO> editNickname(@RequestBody UserNameDTO requestDTO) {
        return Response.success("닉네임 수정 완료", new UserNameDTO());
    }

    @PostMapping("/account")
    @Operation(summary = "계좌 번호 등록")
    public Response<UserAccountDTO> registerAccountNumber(@RequestBody String accountNumber) {
        return Response.success("계좌 번호 등록 완료", new UserAccountDTO());
    }

    @PutMapping("/account")
    @Operation(summary = "계좌 번호 수정")
    public Response<UserAccountDTO> editAccountNumber(@RequestBody String accountNumber) {
        return Response.success("계좌 번호 수정 완료", new UserAccountDTO());
    }

    @PostMapping("/address")
    @Operation(summary = "주소 등록")
    public Response<UserAddressDTO> registerAddress(@RequestBody String address) {
        return Response.success("주소 등록 완료", new UserAddressDTO());
    }

    @PutMapping("/address")
    @Operation(summary = "주소 수정")
    public Response<UserAddressDTO> editAddress(@RequestBody String address) {
        return Response.success("주소 수정 완료", new UserAddressDTO());
    }

}
