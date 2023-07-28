package com.wbbb.steam.dto.response.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String nickname;
    private String avatar;

    /** 用户关系：0——非好友，1——已邀请，2——好友，3——自己 */
    private Integer status;
}
