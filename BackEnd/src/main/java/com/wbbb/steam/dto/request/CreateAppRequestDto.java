package com.wbbb.steam.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateAppRequestDto {
    /**
     * 名称
     */
    private String name;

    /**
     * 封面图片
     */
    private String cover;

    /**
     * 标题图片
     */
    private String header;

    /**
     * 图片
     */
    private List<String> images;

    /**
     * 介绍
     */
    private String description;

    /**
     * 开发商
     */
    private String developer;

    /**
     * 发行商
     */
    private String publisher;

    /**
     * 价格
     */
    private Double price;

    /**
     * 当前价格
     */
    @Column(name = "final_price")
    private Double finalPrice;

    /**
     * 是否支持Windows
     */
    private Boolean win;

    /**
     * 是否支持MacOS
     */
    private Boolean mac;

    /**
     * 是否支持Linux
     */
    private Boolean linux;

}
