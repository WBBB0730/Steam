package com.wbbb.steam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SortWishlistDto {
    private Long appId;
    private Integer sort;
}
