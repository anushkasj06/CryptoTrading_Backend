// src/main/java/com/zosh/config/WebSocketConfig.java
package com.zosh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // The endpoint React will use to connect
        registry.addEndpoint("/ws-community")
                .setAllowedOrigins(
                        "http://localhost:5173",
                        "https://crypto-trading-frontend-new.vercel.app")
                .withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic"); // Broadcast prefix
    }
}