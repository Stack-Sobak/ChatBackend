package top.jgroup.chatbackend.entity.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chatId;
    private String sender;
    @Column(length = 5000)
    private String content;
    private LocalDateTime timestamp;
}