package top.jgroup.chatbackend.entity.models;

import jakarta.persistence.*;
import lombok.Data;
import top.jgroup.chatbackend.entity.enums.ChatType;

import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
@Data
public class Chat {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    private Long botId;

    @Column(length = 5000)
    private String lastMessage;

    private String lastSender;
    private LocalDateTime lastTime;
}
