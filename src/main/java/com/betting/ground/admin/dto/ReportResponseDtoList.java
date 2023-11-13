package com.betting.ground.admin.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDtoList {
    private List<ReportResponseDto> reportResDTOS;
}
