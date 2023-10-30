package com.betting.ground.kakaopay.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class KakaoReadyResponse {

    @Schema(description = "결제 고유 번호", example = "T1234567890123456789")
    private String tid;
    @Schema(description = "웹 결제 요청 url", example = "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo")
    private String next_redirect_pc_url;
    @Schema(description = "모바일 결제 요청 url", example = "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo")
    private String next_redirect_mobile_url;
    @Schema(description = "결제 준비 요청 시간", example = "2016-11-15T21:18:22")
    private String created_at;
    @Schema(description = "로그인 유저 아이디", example = "3")
    private Long userId;
}