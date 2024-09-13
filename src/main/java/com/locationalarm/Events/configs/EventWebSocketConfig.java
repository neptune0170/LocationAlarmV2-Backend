package com.locationalarm.Events.configs;

import com.locationalarm.Events.controllers.EventTrackingWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class EventWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new EventTrackingWebSocketHandler(), "/events/tracking")
                .setAllowedOrigins("*");  // Allow all origins for WebSocket connections
    }
}
