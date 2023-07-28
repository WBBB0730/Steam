package com.wbbb.steam.config;

import com.wbbb.steam.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/** 拦截器 */
@AllArgsConstructor
public class ChatHandshakeInterceptor implements HandshakeInterceptor {
    private final UserService userService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Long userId = null;
        String[] queries = request.getURI().getQuery().split("&");
        for (String query : queries)
            if (query.startsWith("userId="))
                userId = Long.valueOf(query.substring(7));
        if (!userService.isUserIdExists(userId))
            return false;
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
