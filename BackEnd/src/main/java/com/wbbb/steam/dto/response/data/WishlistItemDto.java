package com.wbbb.steam.dto.response.data;

import com.wbbb.steam.entity.App;
import com.wbbb.steam.entity.WishlistItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WishlistItemDto extends AppDto {
    private Integer sort;
    private Long addTime;

    public WishlistItemDto(App app, WishlistItem wishlistItem) {
        super(app);
        this.sort = wishlistItem.getSort();
        this.addTime = wishlistItem.getCreateTime();
    }
}
