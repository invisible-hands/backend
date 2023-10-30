package com.betting.ground.deal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DealCountResponse {

    @Schema(description = "전체", example = "12")
    private int all;
    @Schema(description = "배송 전", example = "0")
    private int before;
    @Schema(description = "진행중", example = "8")
    private int progress;
    @Schema(description = "완료", example = "4")
    private int complete;

    public static DealCountResponse purchases(int all, int process, int complete){
        return new DealCountResponse(all, 0, process, complete);
    }

    public static DealCountResponse bids(int all, int process, int complete){
        return new DealCountResponse(all, 0, process, complete);
    }

    public static DealCountResponse sales(int all, int before, int process, int complete){
        return new DealCountResponse(all, before, process, complete);
    }

    private DealCountResponse(int all, int before, int progress, int complete) {
        this.all = all;
        this.before = before;
        this.progress = progress;
        this.complete = complete;
    }
}
