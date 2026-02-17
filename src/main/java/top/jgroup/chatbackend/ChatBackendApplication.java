package top.jgroup.chatbackend;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.repositories.BotRepository;

@SpringBootApplication
@RequiredArgsConstructor
public class ChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatBackendApplication.class, args);
    }

}
