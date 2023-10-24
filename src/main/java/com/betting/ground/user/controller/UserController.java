package com.betting.ground.user.controller;
import com.betting.ground.common.dto.Response;
import com.betting.ground.user.dto.UserAccountDTO;
import com.betting.ground.user.dto.UserAddressDTO;
import com.betting.ground.user.dto.UserDTO;
import com.betting.ground.user.dto.UserNicknameDTO;
import com.betting.ground.user.dto.response.BiddingInfoResponse;
import com.betting.ground.user.dto.response.PurchaseInfoResponse;
import com.betting.ground.user.dto.response.SalesInfoResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

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
    public Response<UserNicknameDTO> editNickname(@RequestBody UserNicknameDTO requestDTO) {
        return Response.success("닉네임 수정 완료", new UserNicknameDTO());
    }

    @PostMapping("/account")
    @Operation(summary = "계좌 번호 등록")
    public Response<UserAccountDTO> registerAccountNumber(@RequestBody UserAccountDTO accountNumber) {
        return Response.success("계좌 번호 등록 완료", new UserAccountDTO());
    }

    @PutMapping("/account")
    @Operation(summary = "계좌 번호 수정")
    public Response<UserAccountDTO> editAccountNumber(@RequestBody UserAccountDTO accountNumber) {
        return Response.success("계좌 번호 수정 완료", new UserAccountDTO());
    }

    @PostMapping("/address")
    @Operation(summary = "주소 등록")
    public Response<UserAddressDTO> registerAddress(@RequestBody UserAddressDTO address) {
        return Response.success("주소 등록 완료", new UserAddressDTO());
    }

    @PutMapping("/address")
    @Operation(summary = "주소 수정")
    public Response<UserAddressDTO> editAddress(@RequestBody UserAddressDTO address) {
        return Response.success("주소 수정 완료", new UserAddressDTO());
    }

    @GetMapping(value = {"/purchaseList/{status}", "/purchaseList"})
    @Operation(summary = "구매 목록 조회")
    public Response<PurchaseInfoResponse> getPurchaseList(
            @Parameter(description = "filter 기능용 변수명 정해야함")
            @PathVariable(required = false) String status
    ){
        return Response.success("구매 목록 조회 완료", new PurchaseInfoResponse());
    }

    @GetMapping(value = {"/biddingList/{status}", "/biddingList"})
    @Operation(summary = "경매 목록 조회")
    public Response<BiddingInfoResponse> getBiddingList(
            @Parameter(description = "filter 기능용 변수명 정해야함")
            @PathVariable(required = false) String status
    ){
        return Response.success("경매 목록 조회 완료", new BiddingInfoResponse());
    }

    @GetMapping(value = {"/salesList/{status}", "/salesList"})
    @Operation(summary = "판매 목록 조회")
    public Response<SalesInfoResponse> getSalesList(
            @Parameter(description = "filter 기능용 변수명 정해야함")
            @PathVariable(required = false) String status
    ){
        return Response.success("판매 목록 조회 완료", new SalesInfoResponse());
    }
}
