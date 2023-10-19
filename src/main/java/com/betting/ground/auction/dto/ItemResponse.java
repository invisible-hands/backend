package com.betting.ground.auction.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ItemResponse {
    private List<NewItemDto> newItems;
}
