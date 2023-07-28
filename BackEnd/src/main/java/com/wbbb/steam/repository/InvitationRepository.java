package com.wbbb.steam.repository;

import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.entity.FriendId;
import com.wbbb.steam.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, FriendId> {

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    void deleteByUserIdAndFriendId(Long userId, Long friendId);

    /** 获取已发送的好友申请 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.UserDto(u.userId, u.nickname, u.avatar, 1) FROM Invitation i " +
            "JOIN User u ON u.userId = i.friendId " +
            "WHERE i.userId = :userId " +
            "ORDER BY i.createTime DESC")
    List<UserDto> getSentFriendInvitations(Long userId);

    /** 获取未处理的好友申请 */
    @Query("SELECT new com.wbbb.steam.dto.response.data.UserDto(u.userId, u.nickname, u.avatar, 0) FROM Invitation i " +
            "JOIN User u ON u.userId = i.userId " +
            "WHERE i.friendId = :userId " +
            "ORDER BY i.createTime DESC")
    List<UserDto> getPendingFriendInvitations(Long userId);

    /** 获取未处理的好友申请数量 */
    long countByFriendId(Long userId);
}
