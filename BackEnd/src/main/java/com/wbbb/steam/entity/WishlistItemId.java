package com.wbbb.steam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemId implements Serializable {
    private Long userId;
    private Long appId;
}
