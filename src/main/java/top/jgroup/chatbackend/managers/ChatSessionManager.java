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

    // botId -> (scope -> session)
    private final Map<String, Map<String, WebSocketSession>> botSessions =
            new ConcurrentHashMap<>();

    public void addUser(Long chatId, WebSocketSession session) {
        chatSessions
                .computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>())
                .add(session);
    }

    public void addBot(String botId, String scope, WebSocketSession session) {

        botSessions
                .computeIfAbsent(botId, k -> new ConcurrentHashMap<>())
                .put(scope, session);

        System.out.println("Bot " + botId + " registered for scope: " + scope);
    }

    public WebSocketSession getBot(String botId, String scope) {

        Map<String, WebSocketSession> scopes = botSessions.get(botId);

        if (scopes == null) return null;

        return scopes.get(scope);
    }

    public List<WebSocketSession> getUsers(Long chatId) {
        return chatSessions.getOrDefault(chatId, List.of());
    }
}
