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
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품-경매아이템이 존재하지 않습니다.")
    ;

    private final HttpStatus status;
    private final String message;
}
