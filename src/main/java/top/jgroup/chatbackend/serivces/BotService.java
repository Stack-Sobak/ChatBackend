package top.jgroup.chatbackend.serivces;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import top.jgroup.chatbackend.entity.dtos.BotSettingsDto;
import top.jgroup.chatbackend.entity.enums.ChatType;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.repositories.BotRepository;
import top.jgroup.chatbackend.repositories.ChatRepository;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class BotService {

    private final BotRepository botRepository;
    private final ChatRepository chatRepository;
    private final WebClient webClient = WebClient.create();

    public List<Bot> getAll() {
        return botRepository.findAll();
    }

    public Bot create(BotSettingsDto dto) {

        Bot bot = new Bot();
        bot.setName(dto.getName());
        bot.setDescription(dto.getDescription());
        bot.setPrePrompt(dto.getPrePrompt());
        bot.setBaseUrl(dto.getBaseUrl());
        bot.setEnabled(false);

        return botRepository.save(bot);
    }

    public Chat activate(Long id) {

        Bot bot = botRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bot not found"));

        if (bot.isEnabled()) {
            throw new RuntimeException("Bot already active");
        }

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
                        "description", bot.getDescription() == null ? "Empty" : bot.getDescription(),
                        "personality", bot.getPrePrompt() == null ? "Empty" : bot.getPrePrompt(),
                        "participants", List.of("User", bot.getName()),
                        "bot_id", bot.getId()
                ))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return chat;
    }

    public void deactivate(Long id) {

        Bot bot = botRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bot not found"));

        if (!bot.isEnabled()) {
            return;
        }

        bot.setEnabled(false);
        botRepository.save(bot);

        webClient.post()
                .uri(bot.getBaseUrl() + "/deactivate")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
