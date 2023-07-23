package com.wbbb.steam.service;

import com.wbbb.steam.dto.request.SortWishlistDto;
import com.wbbb.steam.dto.response.data.WishlistItemDto;
import com.wbbb.steam.entity.App;
import com.wbbb.steam.entity.WishlistItem;
import com.wbbb.steam.entity.WishlistItemId;
import com.wbbb.steam.repository.AppRepository;
import com.wbbb.steam.repository.WishlistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final AppRepository appRepository;

    public List<WishlistItemDto> getWishlist(Long userId) {
        List<WishlistItem> wishlist = wishlistRepository.findAllByUserId(userId);
        return toWishlistItemDtoList(wishlist);
    }

    public int addAppToWishlist(Long userId, Long appId) {
        if (!appRepository.existsById(appId))
            return 404;
        if (wishlistRepository.existsByUserIdAndAppId(userId, appId))
            return 409;
        wishlistRepository.save(new WishlistItem(userId, appId, 0, System.currentTimeMillis()));
        return 200;
    }

    public int removeAppFromWishlist(Long userId, Long appId) {
        if (!appRepository.existsById(appId) || !wishlistRepository.existsByUserIdAndAppId(userId, appId))
            return 404;
        wishlistRepository.deleteById(new WishlistItemId(userId, appId));
        return 200;
    }

    public void sortWishlist(Long userId, List<SortWishlistDto> list) {
        Map<Long, Integer> map = new HashMap<>();
        for (SortWishlistDto item : list)
            map.put(item.getAppId(), item.getSort());
        List<WishlistItem> wishlist = wishlistRepository.findAllByUserId(userId);
        for (WishlistItem item : wishlist) {
            if (map.containsKey(item.getAppId())) {
                int sort = map.get(item.getAppId());
                item.setSort(sort);
            }
        }
        wishlistRepository.saveAll(wishlist);
    }

    public WishlistItemDto toWishlistItemDto(WishlistItem wishlistItem) {
        App app = appRepository.findById(wishlistItem.getAppId()).orElse(null);
        if (app == null)
            return null;
        WishlistItemDto wishlistItemDto = new WishlistItemDto(app, wishlistItem);
        wishlistItemDto.setStatus(1);
        return wishlistItemDto;
    }

    public List<WishlistItemDto> toWishlistItemDtoList(List<WishlistItem> wishlistItemList) {
        return wishlistItemList.stream()
                .map(this::toWishlistItemDto)
                .collect(Collectors.toList());
    }
}
