package top.jgroup.chatbackend.utlis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.jgroup.chatbackend.entity.enums.ChatType;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.repositories.ChatRepository;

@Component
@RequiredArgsConstructor
public class GlobalChatInitializer implements CommandLineRunner {

    private final ChatRepository chatRepository;

    @Override
    public void run(String... args) {

        if (!chatRepository.existsById(10L)) {

            Chat chat = new Chat();
            chat.setId(10L);
            chat.setType(ChatType.GROUP);
            chat.setLastMessage("Global chat created");
            chat.setLastSender("System");

            chatRepository.save(chat);

            System.out.println("✅ Global chat 10 created");
        }
    }
}

