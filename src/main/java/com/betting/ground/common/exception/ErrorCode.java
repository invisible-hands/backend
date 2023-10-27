package com.betting.ground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    AUCTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재 하지 않는 게시글입니다."),
    AUCTION_TIME_OUT(HttpStatus.BAD_REQUEST, "경매 시간 만료"),
    DEAL_NOT_FOUND(HttpStatus.BAD_REQUEST, "거래 내역이 존재하지 않습니다."),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"지원하지 않는 토큰입니다."),

    USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED,"계정이 존재하지 않습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "비밀번호가 불일치 합니다."),

    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰시간이 만료되었습니다. 다시 로그인 해주세요.")
    ;

    private final HttpStatus status;
    private final String message;
}
