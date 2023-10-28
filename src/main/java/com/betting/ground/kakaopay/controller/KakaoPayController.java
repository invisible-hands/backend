package com.betting.ground.kakaopay.controller;

import com.betting.ground.common.dto.Response;
import com.betting.ground.common.exception.ErrorCode;
import com.betting.ground.common.exception.GlobalException;
import com.betting.ground.kakaopay.dto.request.KakaoReadyRequest;
import com.betting.ground.kakaopay.dto.response.KakaoApproveResponse;
import com.betting.ground.kakaopay.dto.response.KakaoReadyResponse;
import com.betting.ground.kakaopay.service.KakaoPayService;
import com.betting.ground.user.dto.login.LoginUser;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Tag(name = "카카오 페이 결제", description = "카카오 결제 요청")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public KakaoReadyResponse payReady(
            @RequestBody KakaoReadyRequest request,
            @AuthenticationPrincipal LoginUser loginUser
    ){
        return kakaoPayService.kakaoPayReady(request, loginUser.getUser().getId());
    }

    @GetMapping("/success")
    public Response<KakaoApproveResponse> afterPayRequest(@RequestParam(value = "pg_token") String pgToken) {
        return Response.success("결제 완료", kakaoPayService.approveResponse(pgToken));
    }

    @Hidden
    @GetMapping("/cancel")
    public void cancel() {
        throw new GlobalException(ErrorCode.PAY_CANCEL);
    }

    @Hidden
    @GetMapping("/fail")
    public void fail() {
        throw new GlobalException(ErrorCode.PAY_FAILED);
    }
}
