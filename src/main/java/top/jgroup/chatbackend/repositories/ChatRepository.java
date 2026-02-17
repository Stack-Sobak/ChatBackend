package top.jgroup.chatbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jgroup.chatbackend.entity.models.Chat;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByOrderByLastTimeDesc();

}
