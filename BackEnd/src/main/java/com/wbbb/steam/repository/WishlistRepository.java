package com.wbbb.steam.repository;

import com.wbbb.steam.dto.response.data.WishlistItemDto;
import com.wbbb.steam.entity.WishlistItem;
import com.wbbb.steam.entity.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistItem, WishlistItemId> {
    List<WishlistItem> findAllByUserId(Long userId);

    /** 获取愿望单 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.WishlistItemDto(a, w) FROM WishlistItem w " +
            "JOIN App a ON w.appId = a.appId " +
            "WHERE w.userId = :userId")
    List<WishlistItemDto> getWishlist(Long userId);

    int countAllByUserId(Long userId);

    boolean existsByUserIdAndAppId(Long userId, Long appId);
}
