package com.carhutchat.config;

import com.carhutchat.handlers.ChatWebSocketHandler;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private  ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/chat")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    private static class HttpSessionHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(org.springframework.http.server.ServerHttpRequest request,
                                       org.springframework.http.server.ServerHttpResponse response,
                                       org.springframework.web.socket.WebSocketHandler wsHandler,
                                       Map<String, Object> attributes) {
            if (request instanceof org.springframework.http.server.ServletServerHttpRequest) {
                HttpSession session = ((org.springframework.http.server.ServletServerHttpRequest) request).getServletRequest().getSession();
                String username = ((org.springframework.http.server.ServletServerHttpRequest) request).getServletRequest().getParameter("username");
                if (username != null) {
                    attributes.put("username", username);
                }
            }
            return true;
        }

        @Override
        public void afterHandshake(org.springframework.http.server.ServerHttpRequest request,
                                   org.springframework.http.server.ServerHttpResponse response,
                                   org.springframework.web.socket.WebSocketHandler wsHandler,
                                   Exception exception) {
            // No-op
        }
    }

}
