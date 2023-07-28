package com.wbbb.steam.repository;

import com.wbbb.steam.dto.response.data.ChatFriendDto;
import com.wbbb.steam.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT NEW com.wbbb.steam.dto.response.data.ChatFriendDto(u.userId, u.nickname, u.avatar, " +
            "CAST((SELECT COUNT(c) FROM ChatMessage c WHERE c.fromId = f.friendId AND c.toId = f.userId AND c.isRead = FALSE) AS LONG), " +
            "FALSE, MIN(c.id), MAX(c.createTime)) FROM Friend f " +
            "JOIN User u ON u.userId = f.friendId " +
            "LEFT JOIN ChatMessage c ON (c.fromId = f.friendId AND c.toId = :userId) OR (c.fromId = :userId AND c.toId = f.friendId)" +
            "WHERE f.userId = :userId " +
            "GROUP BY u.userId")
    List<ChatFriendDto> getChatFriends(Long userId);

    @Query("SELECT c FROM ChatMessage c " +
            "WHERE (c.fromId = :userId AND c.toId = :friendId) OR (c.fromId = :friendId AND c.toId = :userId) " +
            "ORDER BY c.createTime DESC")
    List<ChatMessage> getChatMessages(Long userId, Long friendId, Pageable pageable);

    @Query("SELECT c FROM ChatMessage c " +
            "WHERE ((c.fromId = :userId AND c.toId = :friendId) OR (c.fromId = :friendId AND c.toId = :userId)) AND c.id < :id " +
            "ORDER BY c.createTime DESC")
    List<ChatMessage> getChatMessages(Long userId, Long friendId, Long id, Pageable pageable);

    @Modifying
    @Query("UPDATE ChatMessage c SET c.isRead = TRUE " +
            "WHERE c.fromId = :friendId AND c.toId = :userId AND c.id <= :id")
    void setChatMessagesRead(Long userId, Long friendId, Long id);
}
