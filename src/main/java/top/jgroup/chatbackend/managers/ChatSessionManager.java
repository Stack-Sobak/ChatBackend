package top.jgroup.chatbackend.managers;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatSessionManager {

    private final Map<Long, List<WebSocketSession>> chatSessions =
            new ConcurrentHashMap<>();

    private final Map<String, WebSocketSession> botSessions =
            new ConcurrentHashMap<>();

    public void addUser(Long chatId, WebSocketSession session) {
        chatSessions
                .computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>())
                .add(session);
    }

    public void addBot(String botId, WebSocketSession session) {
        botSessions.put(botId, session);
    }

    public List<WebSocketSession> getUsers(Long chatId) {
        return chatSessions.getOrDefault(chatId, List.of());
    }

    public WebSocketSession getBot(String botId) {
        return botSessions.get(botId);
    }
}
