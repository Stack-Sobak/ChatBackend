package top.jgroup.chatbackend.managers;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotSessionManager {

    private final Map<String, WebSocketSession> sessions =
            new ConcurrentHashMap<>();

    public void add(String botId, WebSocketSession session) {
        sessions.put(botId, session);
    }

    public WebSocketSession get(String botId) {
        return sessions.get(botId);
    }
}