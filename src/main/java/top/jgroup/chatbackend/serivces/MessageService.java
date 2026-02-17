package top.jgroup.chatbackend.serivces;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import top.jgroup.chatbackend.entity.dtos.BotMessageDto;
import top.jgroup.chatbackend.entity.dtos.SendMessageDto;
import top.jgroup.chatbackend.entity.enums.ChatType;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.entity.models.Message;
import top.jgroup.chatbackend.managers.BotSessionManager;
import top.jgroup.chatbackend.repositories.BotRepository;
import top.jgroup.chatbackend.repositories.ChatRepository;
import top.jgroup.chatbackend.repositories.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final BotRepository botRepository;
    private final BotSessionManager sessionManager;

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

            sendToBot(bot.getId().toString(), dto.getContent());
        }

        if (chat.getType() == ChatType.GROUP) {

            List<Bot> bots = botRepository.findAllByEnabledTrue();

            for (Bot bot : bots) {
                sendToBot(bot.getId().toString(), dto.getContent());
            }
        }
    }

    private void sendToBot(String botId, String content) {

        WebSocketSession session = sessionManager.get(botId);

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(
                        new TextMessage(
                                "{\"sender\":\"User\",\"content\":\""
                                        + content + "\"}"
                        )
                );
            } catch (Exception ignored) {}
        }
    }

    public void saveBotMessage(BotMessageDto dto) {

        // 1️⃣ Сохраняем сообщение
        Message message = new Message();
        message.setChatId(dto.getChatId());
        message.setSender(dto.getSender());
        message.setContent(dto.getContent());
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        // 2️⃣ Обновляем превью чата
        Chat chat = chatRepository.findById(dto.getChatId())
                .orElseThrow();

        chat.setLastMessage(dto.getContent());
        chat.setLastSender(dto.getSender());
        chat.setLastTime(LocalDateTime.now());

        chatRepository.save(chat);
    }
}