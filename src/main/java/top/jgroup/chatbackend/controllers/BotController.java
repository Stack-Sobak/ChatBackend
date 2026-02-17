package top.jgroup.chatbackend.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jgroup.chatbackend.entity.models.Bot;
import top.jgroup.chatbackend.entity.models.Chat;
import top.jgroup.chatbackend.serivces.BotActivationService;

@RestController
@RequestMapping("/api/bots")
public class BotController {


    private final BotActivationService activationService;


    public BotController(BotActivationService activationService) {
        Bot bot = new Bot();
        bot.setBaseUrl("http://localhost:8000");
        bot.setName("Test Bot");

        this.activationService = activationService;
    }


    @PostMapping("/{id}/activate")
    public Chat activate(@PathVariable Long id) {
        System.out.println("Activating bot with id: " + id);
        return activationService.activateBot(id);
    }
}

