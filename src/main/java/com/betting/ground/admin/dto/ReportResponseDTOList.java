package com.betting.ground.admin.dto;

import lombok.*;

import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDTOList {
    private List<ReportResponseDTO> reportResDTOS;
}
