package com.betting.ground.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Response<T> {

    @Schema(description = "응답 상태값", example="Success")
    private String status;
    @Schema(description = "어떤 api 인지 설명", example = "어떤 api 인지 설명")
    private String message;
    @Schema(description = "응답 데이터")
    private T data;

    public static <T> Response<T> success(String message, T data){
        return new Response<>("Success", message, data);
    }

    public static Response<Void> error(String status, String message){
        return new Response<>(status, message, null);
    }
}
