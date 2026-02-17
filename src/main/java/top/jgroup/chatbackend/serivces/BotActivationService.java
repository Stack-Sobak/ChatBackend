package top.jgroup.chatbackend.serivces;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import top.jgroup.chatbackend.entity.enums.ChatType;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.repositories.BotRepository;
import top.jgroup.chatbackend.repositories.ChatRepository;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BotActivationService {

    private final BotRepository botRepository;
    private final ChatRepository chatRepository;
    private final WebClient webClient;

    public BotActivationService(BotRepository botRepository,
                                ChatRepository chatRepository) {
        this.botRepository = botRepository;
        this.chatRepository = chatRepository;
        this.webClient = WebClient.create();
    }

    public Chat activateBot(Long botId) {

        Bot bot = botRepository.findById(botId)
                .orElseGet(() -> {
                    Bot newBot = new Bot();
                    newBot.setName("Bot " + botId);
                    newBot.setBaseUrl("http://localhost:8000");
                    newBot.setEnabled(false);
                    return botRepository.save(newBot);
                });

        Chat chat = new Chat();
        chat.setType(ChatType.PRIVATE);
        chat.setBotId(bot.getId());

        chat = chatRepository.save(chat);

        bot.setEnabled(true);
        bot.setPrivateChatId(chat.getId());
        botRepository.save(bot);

        webClient.post()
                .uri(bot.getBaseUrl() + "/activate")
                .bodyValue(Map.of(
                        "name", bot.getName(),
                        "description", "AI agent",
                        "personality", "Smart",
                        "participants", List.of("User", "Bot2", "Bot3"),
                        "llm_provider", "ollama"
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return chat;
    }
}