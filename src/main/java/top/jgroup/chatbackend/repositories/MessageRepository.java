package top.jgroup.chatbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jgroup.chatbackend.entity.models.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatIdOrderByTimestampAsc(Long chatId);
}
