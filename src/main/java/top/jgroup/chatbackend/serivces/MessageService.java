package top.jgroup.chatbackend.serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;
import top.jgroup.chatbackend.entity.dtos.BotMessageDto;
import top.jgroup.chatbackend.entity.dtos.SendMessageDto;
import top.jgroup.chatbackend.entity.enums.ChatType;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.entity.models.Message;
import top.jgroup.chatbackend.managers.ChatSessionManager;
import top.jgroup.chatbackend.repositories.BotRepository;
import top.jgroup.chatbackend.repositories.ChatRepository;
import top.jgroup.chatbackend.repositories.MessageRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final BotRepository botRepository;
    private final ChatSessionManager sessionManager;

    private final ObjectMapper mapper = new ObjectMapper();


    public void handleUserMessage(Long chatId,
                                  SendMessageDto dto) {

        Message message = new Message();
        message.setChatId(chatId);
        message.setSender("User");
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        Chat chat = chatRepository.findById(chatId).orElseThrow();

        if (chat.getType() == ChatType.PRIVATE) {

            Bot bot = botRepository.findById(chat.getBotId()).orElseThrow();

            sendToBot(bot.getId().toString(), chatId, dto.getContent(), "private");

        }

        if (chat.getType() == ChatType.GROUP) {

            List<Bot> bots = botRepository.findAllByEnabledTrue();

            for (Bot bot : bots) {
                sendToBot(bot.getId().toString(), chatId, dto.getContent(), "global");
            }
        }
    }

    private void sendToBot(String botId,
                           Long chatId,
                           String content,
                           String scope) {

        WebSocketSession session =
                sessionManager.getBot(botId, scope);

        if (session != null && session.isOpen()) {
            try {

                Map<String, Object> payload = Map.of(
                        "chatId", chatId,
                        "sender", "User",
                        "content", content
                );

                session.sendMessage(
                        new TextMessage(
                                mapper.writeValueAsString(payload)
                        )
                );

            } catch (Exception ignored) {}
        }
    }


    public void saveBotMessage(BotMessageDto dto) throws IOException {

        Message message = new Message();
        message.setChatId(dto.getChatId());
        message.setSender(dto.getSender());
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow();

        chat.setLastMessage(dto.getContent());
        chat.setLastSender(dto.getSender());
        chat.setLastTime(LocalDateTime.now());

        chatRepository.save(chat);

        List<WebSocketSession> users =
                sessionManager.getUsers(dto.getChatId());

        for (WebSocketSession user : users) {
            if (user.isOpen()) {
                user.sendMessage(
                        new TextMessage(
                                "{\"chatId\":" + dto.getChatId() + "," +
                                        "\"sender\":\"" + dto.getSender() + "\"," +
                                        "\"content\":\"" + dto.getContent() + "\"}"
                        )
                );
            }
        }

        if (chat.getType() == ChatType.GROUP) {

            List<Bot> bots = botRepository.findAllByEnabledTrue();

            for (Bot bot : bots) {

                if (!bot.getId().equals(dto.getBotId())) {

                    WebSocketSession botSession =
                            sessionManager.getBot(bot.getId().toString(), "global");

                    if (botSession != null && botSession.isOpen()) {

                        botSession.sendMessage(
                                new TextMessage(
                                        "{\"chatId\":" + dto.getChatId() + "," +
                                                "\"botId\":" + dto.getBotId() + "," +
                                                "\"sender\":\"" + dto.getSender() + "\"," +
                                                "\"content\":\"" + dto.getContent() + "\"}"
                                )
                        );
                    }
                }
            }
        }
    }
}