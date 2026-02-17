package top.jgroup.chatbackend.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BotSettingsDto {

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "Base url cannot be null")
    private String baseUrl;

    private String description;
    private String prePrompt;
}
