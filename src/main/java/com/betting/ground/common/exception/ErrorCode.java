package com.betting.ground.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"지원하지 않는 토큰입니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰시간이 만료되었습니다. 다시 로그인 해주세요."),

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    USERNAME_NOT_FOUND(HttpStatus.UNAUTHORIZED,"계정이 존재하지 않습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "비밀번호가 불일치 합니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    AUCTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재 하지 않는 게시글입니다."),
    AUCTION_SOLD_OUT(HttpStatus.BAD_REQUEST,"이미 상품이 판매되었습니다."),
    AUCTION_TIME_OUT(HttpStatus.BAD_REQUEST, "경매 시간 만료"),
    ALREADY_AUCTION_START(HttpStatus.BAD_REQUEST, "시작된 경매 입니다."),
    AUCTION_NOT_START(HttpStatus.BAD_REQUEST, "경매가 시작되지 않았습니다."),

    DEAL_NOT_FOUND(HttpStatus.BAD_REQUEST, "거래 내역이 존재하지 않습니다."),

    PAY_CANCEL(HttpStatus.BAD_REQUEST, "유저 결제 취소"),
    PAY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 실패"),

    NOT_ENOUGH_MONEY(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),
    CAN_NOT_PURCHASE(HttpStatus.BAD_REQUEST, "본인이 올린 경매글은 구매할 수 없습니다."),
    CAN_NOT_DELETE(HttpStatus.BAD_REQUEST, "본인이 올린 경매글은 삭제할 수 없습니다."),
    ALREADY_TOP_PRICE_BIDDER(HttpStatus.BAD_REQUEST, "이미 최고 입찰가로 참여하셨습니다."),
    EXCEED_INSTANT_PRICE(HttpStatus.BAD_REQUEST, "즉시 구매가를 초과하였습니다."),
    LESS_THEN_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "입찰가가 현재가보다 작습니다"),
    ;


    private final HttpStatus status;
    private final String message;
}
