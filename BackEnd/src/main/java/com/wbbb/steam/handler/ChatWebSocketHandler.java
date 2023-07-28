package com.wbbb.steam.handler;

import com.google.gson.Gson;
import com.wbbb.steam.dto.response.data.ChatFriendDto;
import com.wbbb.steam.entity.ChatMessage;
import com.wbbb.steam.service.ChatService;
import com.wbbb.steam.service.FriendService;
import com.wbbb.steam.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private static final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final UserService userService;
    private final FriendService friendService;
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null)
            session.close();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        Gson gson = new Gson();
        JSONParser jsonParser = new JSONParser(message.getPayload());
        LinkedHashMap<String, Object> jsonMap = jsonParser.parseObject();

        Long userId = (Long) session.getAttributes().get("userId");
        String type = (String) jsonMap.get("type");
        String content = (String) jsonMap.get("content");
        String extra = (String) jsonMap.get("extra");

        switch (type) {
            case "connect": {   // 身份校验
                Long tokenUserId = userService.parseToken(content);
                if (!userId.equals(tokenUserId)) {
                    session.getAttributes().remove("userId");
                    session.close();
                    return;
                }
                WebSocketSession s = sessions.get(userId);
                if (s != null) {
                    s.getAttributes().remove("userId");
                    s.close();
                }
                sessions.put(userId, session);
                break;
            }
            case "friends": {   // 获取好友列表
                List<ChatFriendDto> friends = chatService.getChatFriends(userId);
                for (ChatFriendDto friend : friends) {
                    WebSocketSession s = sessions.get(friend.getUserId());
                    if (s != null) {
                        friend.setOnline(true);
                        // 向在线好友发送上线通知
                        sendMessage(s, "online", userId);
//                        s.sendMessage(new TextMessage(gson.toJson(userId)));
                    }
                }
                sendMessage(session, "friends", friends);
//                session.sendMessage(new TextMessage(gson.toJson(friends)));
                // 保存好友列表
                String friendIds = String.join(";", friends.stream().map(friend -> friend.getUserId().toString()).toList());
                session.getAttributes().put("friends", friendIds);
                break;
            }
            case "fetch": {     // 获取聊天记录
                Long friendId = Long.valueOf(content);
                Long id = extra != null ? Long.valueOf(extra) : null;
                List<ChatMessage> chatMessages = chatService.getChatMessages(userId, friendId, id, 50);
                Long lastReadChatMessageId = 0L;
                for (ChatMessage chatMessage : chatMessages) {
                    if (chatMessage.getToId().equals(userId) && !chatMessage.isRead()) {
                        chatMessage.setRead(true);
                        lastReadChatMessageId = Math.max(lastReadChatMessageId, chatMessage.getId());
                    }
                    sendMessage(session, "message", chatMessage);
//                    session.sendMessage(new TextMessage(gson.toJson(chatMessage)));
                }
                if (lastReadChatMessageId != 0L)
                    chatService.setChatMessagesRead(userId, friendId, lastReadChatMessageId);
                break;
            }
            case "read": {      // 已读
                Long id = Long.valueOf(content);
                Long friendId = Long.valueOf(extra);
                chatService.setChatMessagesRead(userId, friendId, id);
                break;
            }
            case "message": {   // 发送消息
                // 缺少字段
                if (content == null || content.length() == 0 || extra == null || extra.length() == 0)
                    return;

                Long friendId = Long.valueOf(extra);

                // 对方不是好友
                if (!friendService.isFriend(userId, friendId))
                    return;

                // 发送消息
                ChatMessage chatMessage = chatService.sendChatMessage(userId, friendId, content);
                sendMessage(session, "message", chatMessage);
//                session.sendMessage(new TextMessage(gson.toJson(chatMessage)));

                // 对方在线
                WebSocketSession s = sessions.get(friendId);
                if (s != null)
                    sendMessage(s, "message", chatMessage);
//                    s.sendMessage(new TextMessage(gson.toJson(chatMessage)));

                break;
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println(session.getAttributes().get("userId") + " close");
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null)
            return;
        sessions.remove(userId);
        // 向在线好友发送下线通知
        Gson gson = new Gson();
        String friends = (String) session.getAttributes().get("friends");
        for (String friend : friends.split(";")) {
            long friendId = Long.parseLong(friend);
            WebSocketSession s = sessions.get(friendId);
            if (s != null)
                sendMessage(s, "offline", userId);
//                s.sendMessage(new TextMessage(gson.toJson(-userId)));
        }
    }

    void sendMessage(WebSocketSession session, String type, Object data) throws Exception {
        Gson gson = new Gson();
        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("type", type);
        jsonMap.put("data", data);

        session.sendMessage(new TextMessage(gson.toJson(jsonMap)));
    }
}
