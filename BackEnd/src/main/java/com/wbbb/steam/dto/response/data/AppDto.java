package com.wbbb.steam.dto.response.data;

import com.wbbb.steam.entity.App;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppDto {
    /**
     * 游戏id
     */
    private Long appId;

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
    private Double finalPrice;

    /**
     * 折扣
     */
    private Double discount;

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

    /**
     * 状态：0——未拥有，1——在愿望单中，2——在库中
     */
    private Integer status = 0;

    /**
     * 发行时间
     */
    private Long releaseTime;

    public AppDto(App app) {
        this.appId = app.getAppId();
        this.name = app.getName();
        this.cover = app.getCover();
        this.header = app.getHeader();
        this.images = List.of(app.getImages().split(";"));
        this.description = app.getDescription();
        this.developer = app.getDeveloper();
        this.publisher = app.getPublisher();
        this.price = app.getPrice();
        this.finalPrice = app.getFinalPrice();
        this.discount = this.price != 0 ? (this.price - this.finalPrice) / this.price : 0;
        this.win = app.getWin();
        this.mac = app.getMac();
        this.linux = app.getLinux();
        this.releaseTime= app.getCreateTime();
    }
}
