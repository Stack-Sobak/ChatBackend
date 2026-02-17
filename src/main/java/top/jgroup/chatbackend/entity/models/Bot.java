package top.jgroup.chatbackend.entity.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bots")
public class Bot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private String prePrompt;

    private String baseUrl;

    private boolean enabled;
    private Long privateChatId;
}
