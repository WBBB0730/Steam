package com.wbbb.steam.repository;

import com.wbbb.steam.dto.response.data.AppDto;
import com.wbbb.steam.entity.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppRepository extends JpaRepository<App, Long> {

    /** 获取推荐列表 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.AppDto(a, CASE WHEN w IS NOT NULL THEN 1 ELSE 0 END) FROM App a " +
            "LEFT JOIN WishlistItem w ON a.appId = w.appId AND w.userId = :userId")
    Page<AppDto> getRecommendations(Pageable pageable, Long userId);

    /** 获取游戏详情 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.AppDto(a, CASE WHEN w IS NOT NULL THEN 1 ELSE 0 END) FROM App a " +
            "LEFT JOIN WishlistItem w ON a.appId = w.appId AND w.userId = :userId " +
            "WHERE a.appId = :appId")
    AppDto getApp(Long appId, Long userId);

    /** 搜索游戏 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.AppDto(a, CASE WHEN w IS NOT NULL THEN 1 ELSE 0 END) FROM App a " +
            "LEFT JOIN WishlistItem w ON a.appId = w.appId AND w.userId = :userId " +
            "WHERE LOWER(a.name) LIKE %:keyword% " +
            "OR LOWER(a.developer) LIKE %:keyword% " +
            "OR LOWER(a.publisher) LIKE %:keyword%")
    List<AppDto> searchApp(String keyword, Long userId);
}
