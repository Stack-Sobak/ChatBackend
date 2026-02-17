package top.jgroup.chatbackend.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;
import top.jgroup.chatbackend.entity.dtos.BotMessageDto;
import top.jgroup.chatbackend.managers.ChatSessionManager;
import top.jgroup.chatbackend.serivces.MessageService;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotSocketHandler extends TextWebSocketHandler {

    private final ChatSessionManager sessionManager;
    private final MessageService messageService;

    public BotSocketHandler(ChatSessionManager manager,
                            MessageService messageService) {
        this.sessionManager = manager;
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        String query = session.getUri().getQuery();

        Map<String, String> params = parseQuery(query);

        if ("bot".equals(params.get("type"))) {
            sessionManager.addBot(params.get("botId"), session);
        }

        if ("user".equals(params.get("type"))) {
            Long chatId = Long.valueOf(params.get("chatId"));
            sessionManager.addUser(chatId, session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        BotMessageDto dto =
                mapper.readValue(message.getPayload(), BotMessageDto.class);

        messageService.saveBotMessage(dto);

        WebSocketSession botSession =
                sessionManager.getBot(dto.getChatId().toString());

        if (botSession != null && botSession.isOpen()) {
            botSession.sendMessage(
                    new TextMessage(mapper.writeValueAsString(dto))
            );
        }
    }


    private Map<String, String> parseQuery(String query) {

        Map<String, String> result = new HashMap<>();

        if (query == null || query.isEmpty()) {
            return result;
        }

        String[] pairs = query.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            }
        }

        return result;
    }
}