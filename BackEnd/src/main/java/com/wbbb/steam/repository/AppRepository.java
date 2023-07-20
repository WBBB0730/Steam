package com.wbbb.steam.repository;

import com.wbbb.steam.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppRepository extends JpaRepository<App, Long> {
    /**
     * 搜索
     */
    List<App> findAllByNameContainingIgnoreCaseOrDeveloperContainingIgnoreCaseOrPublisherContainingIgnoreCase(String nameKeyword, String developerKeyword, String publisherKeyword);

//    @Query("SELECT COUNT(w) FROM WishlistItem w WHERE w.appId = :appId AND w.userId = :userId")
//    boolean isAppInWishList(Long appId, Long userId);
}
