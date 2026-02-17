package top.jgroup.chatbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import top.jgroup.chatbackend.entity.models.Bot;

import java.util.List;

public interface BotRepository extends JpaRepository<Bot, Long> {
    List<Bot> findAllByEnabledTrue();
}
