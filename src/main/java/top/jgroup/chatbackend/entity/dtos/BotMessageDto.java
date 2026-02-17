package top.jgroup.chatbackend.entity.dtos;

import lombok.Data;

@Data
public class BotMessageDto {

    private Long chatId;
    private String sender;
    private String content;
}
