package com.wbbb.steam.dto.response.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/** 聊天列表好友 */
@Getter
@Setter
@AllArgsConstructor
public class ChatFriendDto {
    private Long userId;
    private String nickname;
    private String avatar;

    /** 未读消息数量 */
    private Long unreadNum;

    /** 是否在线 */
    private Boolean online;

    /** 第一天的消息的id */
    private Long firstMessageId;

    /** 最后一次消息时间 */
    private Long lastMessageTime;
}
