package com.wbbb.steam.service;

import com.wbbb.steam.dto.request.SortWishlistRequestDto;
import com.wbbb.steam.dto.response.data.WishlistItemDto;
import com.wbbb.steam.entity.WishlistItem;
import com.wbbb.steam.entity.WishlistItemId;
import com.wbbb.steam.repository.AppRepository;
import com.wbbb.steam.repository.WishlistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final AppRepository appRepository;

    /** 获取愿望单 */
    public List<WishlistItemDto> getWishlist(Long userId) {
        return wishlistRepository.getWishlist(userId);
    }

    /** 获取愿望单物品数量 */
    public Integer getWishlistSize(Long userId) {
        return wishlistRepository.countAllByUserId(userId);
    }

    /** 添加到愿望单 */
    public int addAppToWishlist(Long userId, Long appId) {
        if (!appRepository.existsById(appId))
            return 404;
        if (wishlistRepository.existsByUserIdAndAppId(userId, appId))
            return 409;
        wishlistRepository.save(new WishlistItem(userId, appId, 0, System.currentTimeMillis()));
        return 200;
    }

    /** 从愿望单移除 */
    public int removeAppFromWishlist(Long userId, Long appId) {
        if (!appRepository.existsById(appId) || !wishlistRepository.existsByUserIdAndAppId(userId, appId))
            return 404;
        wishlistRepository.deleteById(new WishlistItemId(userId, appId));
        return 200;
    }

    /** 用户自定义排序 */
    public void sortWishlist(Long userId, List<SortWishlistRequestDto> list) {
        Map<Long, Integer> map = new HashMap<>();
        for (SortWishlistRequestDto item : list)
            map.put(item.getAppId(), item.getSort());
        List<WishlistItem> wishlist = wishlistRepository.findAllByUserId(userId);
        for (WishlistItem item : wishlist) {
            if (map.containsKey(item.getAppId())) {
                Integer sort = map.get(item.getAppId());
                item.setSort(sort);
            }
        }
        wishlistRepository.saveAll(wishlist);
    }
}
