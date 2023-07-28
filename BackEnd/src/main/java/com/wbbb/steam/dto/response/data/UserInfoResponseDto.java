package com.wbbb.steam.dto.response.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponseDto {
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
}
