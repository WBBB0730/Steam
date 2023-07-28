package com.wbbb.steam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SortWishlistRequestDto {
    private Long appId;
    private Integer sort;
}
