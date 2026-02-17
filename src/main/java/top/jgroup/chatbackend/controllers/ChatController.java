package top.jgroup.chatbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.jgroup.chatbackend.entity.dtos.ChatPreviewDto;
import top.jgroup.chatbackend.entity.dtos.SendMessageDto;
import top.jgroup.chatbackend.entity.models.Message;
import top.jgroup.chatbackend.repositories.ChatRepository;
import top.jgroup.chatbackend.repositories.MessageRepository;
import top.jgroup.chatbackend.serivces.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @PostMapping("/{chatId}/send")
    public void send(@PathVariable Long chatId,
                     @RequestBody SendMessageDto dto) {

        messageService.handleUserMessage(chatId, dto);
    }

    @GetMapping
    public List<ChatPreviewDto> getAllChats() {

        return chatRepository.findAllByOrderByLastTimeDesc()
                .stream()
                .map(chat -> {
                    ChatPreviewDto dto = new ChatPreviewDto();
                    dto.setId(chat.getId());
                    dto.setType(chat.getType().name());
                    dto.setLastMessage(chat.getLastMessage());
                    dto.setLastSender(chat.getLastSender());
                    dto.setLastTime(chat.getLastTime());
                    return dto;
                })
                .toList();
    }

    @GetMapping("/{chatId}/messages")
    public List<Message> getMessages(@PathVariable Long chatId) {
        return messageRepository
                .findAllByChatIdOrderByTimestampAsc(chatId);
    }
}