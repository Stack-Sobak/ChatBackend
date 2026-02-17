package top.jgroup.chatbackend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.jgroup.chatbackend.entity.dtos.BotSettingsDto;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.serivces.BotService;

import java.util.List;


@RestController
@RequestMapping("/bots")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @GetMapping
    public List<Bot> getAll() {
        return botService.getAll();
    }

    @PostMapping
    public Bot create(@RequestBody @Valid BotSettingsDto dto) {
        return botService.create(dto);
    }

    @PostMapping("/{id}/activate")
    public Chat activate(@PathVariable Long id) {
        return botService.activate(id);
    }

    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        botService.deactivate(id);
    }
}

