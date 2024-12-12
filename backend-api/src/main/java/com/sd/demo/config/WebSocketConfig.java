package com.sd.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Define o prefixo para tópicos e filas
        registry.enableSimpleBroker("/topic"); // Isso permite que os clientes recebam mensagens de /topic/...
        registry.setApplicationDestinationPrefixes("/app"); // Prefixo para mensagens do cliente para o servidor
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define o endpoint WebSocket e permite CORS
        registry.addEndpoint("/chat-websocket").setAllowedOrigins("http://localhost:3000").withSockJS();
    }
}
