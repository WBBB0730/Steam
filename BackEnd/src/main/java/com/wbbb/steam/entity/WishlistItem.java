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
@Table(name = "wishlist_item")
@IdClass(WishlistItemId.class)
public class WishlistItem {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "app_id")
    private Long appId;

    private Integer sort = 0;

    @Column(name = "create_time")
    private Long createTime;
}
