package top.jgroup.chatbackend.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;
import top.jgroup.chatbackend.entity.dtos.BotMessageDto;
import top.jgroup.chatbackend.managers.BotSessionManager;
import top.jgroup.chatbackend.serivces.MessageService;

@Component
public class BotSocketHandler extends TextWebSocketHandler {

    private final BotSessionManager sessionManager;
    private final MessageService messageService;

    public BotSocketHandler(BotSessionManager manager,
                            MessageService messageService) {
        this.sessionManager = manager;
        this.messageService = messageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        String query = session.getUri().getQuery(); // botId=1

        String botId = null;
        if (query != null && query.startsWith("botId=")) {
            botId = query.substring(6);
        }

        sessionManager.add(botId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {

        System.out.println("BOT RESPONSE: " + message.getPayload());

        ObjectMapper mapper = new ObjectMapper();

        BotMessageDto dto =
                mapper.readValue(message.getPayload(), BotMessageDto.class);

        messageService.saveBotMessage(dto);
    }
}