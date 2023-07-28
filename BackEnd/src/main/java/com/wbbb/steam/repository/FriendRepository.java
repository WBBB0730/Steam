package com.wbbb.steam.repository;

import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.entity.Friend;
import com.wbbb.steam.entity.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    /** 获取好友列表 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.UserDto(u.userId, u.nickname, u.avatar, 1) FROM Friend f " +
            "JOIN User u ON f.friendId = u.userId " +
            "WHERE f.userId = :userId")
    List<UserDto> getFriendList(Long userId);

    int countAllByUserId(Long userId);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
}
