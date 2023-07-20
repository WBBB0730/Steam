package com.wbbb.steam.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 账户信息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    /**
     * 账户id
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_id_generator")
    @TableGenerator(name = "user_id_generator", table = "id_generator", pkColumnName = "name", valueColumnName = "value",
            pkColumnValue = "user", initialValue = 1463960129)
    private Long userId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 账户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    public User(String email, String username, String password, String nickname, String avatar, Long createTime) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
        this.createTime = createTime;
    }
}
