package top.jgroup.chatbackend.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final BotSocketHandler botSocketHandler;

    public WebSocketConfig(BotSocketHandler handler) {
        this.botSocketHandler = handler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(botSocketHandler, "/ws/global")
                .setAllowedOrigins("*");

        registry.addHandler(botSocketHandler, "/ws/private")
                .setAllowedOrigins("*");
    }
}