package com.wbbb.steam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class App {
    /** 游戏id */
    @Id
    @Column(name = "app_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "app_id_generator")
    @TableGenerator(name = "app_id_generator", table = "id_generator", pkColumnName = "name", valueColumnName = "value",
            pkColumnValue = "app", initialValue = 20250119)
    private Long appId;

    /** 名称 */
    private String name;

    /** 封面图片 */
    private String cover;

    /** 标题图片 */
    private String header;

    /** 图片 */
    @Column(length = 4095)
    private String images;

    /** 介绍 */
    @Column(length = 4095)
    private String description;

    /** 开发商 */
    private String developer;

    /** 发行商 */
    private String publisher;

    /** 价格 */
    private Double price;

    /** 当前价格 */
    @Column(name = "final_price")
    private Double finalPrice;

    /** 是否支持Windows */
    private Boolean win;

    /** 是否支持MacOS */
    private Boolean mac;

    /** 是否支持Linux */
    private Boolean linux;

    /** 发行时间 */
    @Column(name = "create_time")
    private Long createTime;

    public App(String name, String cover, String header, String images, String description, String developer, String publisher, Double price, Double finalPrice, Boolean win, Boolean mac, Boolean linux, Long createTime) {
        this.name = name;
        this.cover = cover;
        this.header = header;
        this.images = images;
        this.description = description;
        this.developer = developer;
        this.publisher = publisher;
        this.price = price;
        this.finalPrice = finalPrice;
        this.win = win;
        this.mac = mac;
        this.linux = linux;
        this.createTime = createTime;
    }
}
