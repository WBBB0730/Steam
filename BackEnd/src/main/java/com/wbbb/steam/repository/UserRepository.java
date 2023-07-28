package com.wbbb.steam.repository;

import com.wbbb.steam.dto.response.data.UserDto;
import com.wbbb.steam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT new com.wbbb.steam.dto.response.data.UserDto(u.userId, u.nickname, u.avatar, " +
            "CASE WHEN u.userId = :userId THEN 3 ELSE " +
            "CASE WHEN f IS NOT NULL THEN 2 ELSE " +
            "CASE WHEN i IS NOT NULL THEN 1 ELSE 0 END END END) FROM User u " +
            "LEFT JOIN Friend f ON f.userId = :userId AND f.friendId = u.userId " +
            "LEFT JOIN Invitation i ON i.userId = :userId AND i.friendId = u.userId " +
            "WHERE LOWER(u.nickname) LIKE CONCAT('%', LOWER(:keyword) , '%') " +
            "OR u.username = :keyword " +
            "OR u.userId = :keyword")
    Page<UserDto> searchUser(String keyword, Long userId, Pageable pageable);
}
