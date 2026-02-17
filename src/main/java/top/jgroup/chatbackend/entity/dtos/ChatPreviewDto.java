package top.jgroup.chatbackend.entity.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatPreviewDto {

    private Long id;
    private String type; // GLOBAL / PRIVATE
    private String lastMessage;
    private String lastSender;
    private LocalDateTime lastTime;
}
