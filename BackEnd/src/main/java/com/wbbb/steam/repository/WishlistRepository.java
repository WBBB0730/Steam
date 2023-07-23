package com.wbbb.steam.repository;

import com.wbbb.steam.entity.WishlistItem;
import com.wbbb.steam.entity.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistItem, WishlistItemId> {
    List<WishlistItem> findAllByUserId(Long userId);

    int countAllByUserId(Long userId);

    boolean existsByUserIdAndAppId(Long userId, Long appId);
}
