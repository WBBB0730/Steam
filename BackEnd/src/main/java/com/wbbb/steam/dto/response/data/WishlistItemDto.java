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
    /** 用户自定义排序 */
    private Integer sort;

    /** 添加时间 */
    private Long addTime;

    public WishlistItemDto(App app, WishlistItem wishlistItem) {
        super(app, 1);
        this.sort = wishlistItem.getSort();
        this.addTime = wishlistItem.getCreateTime();
    }
}
