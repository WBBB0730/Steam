package com.wbbb.steam.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(FriendId.class)
public class Friend {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "friend_id")
    private Long friendId;

    @Column(name = "create_time")
    private Long createTime;
}
