package com.wbbb.steam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageDto<T> {
    private Long total;
    private Integer pageIndex;
    private Integer pageSize;
    private List<T> list;
}
