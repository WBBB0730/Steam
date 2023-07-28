package com.wbbb.steam.config;

import com.wbbb.steam.handler.ChatWebSocketHandler;
import com.wbbb.steam.service.ChatService;
import com.wbbb.steam.service.FriendService;
import com.wbbb.steam.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@AllArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final UserService userService;
    private final FriendService friendService;
    private final ChatService chatService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(userService, friendService, chatService), "/chat")
                .addInterceptors(new ChatHandshakeInterceptor(userService))
                .setAllowedOrigins("*");
    }
}
