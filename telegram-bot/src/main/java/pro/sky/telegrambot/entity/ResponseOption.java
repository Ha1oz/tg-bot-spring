package pro.sky.telegrambot.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ResponseOption {
    UNKNOWN_COMMAND(1, "Command is unknown. Please try again."),
    ONLY_COMMANDS(2, "I don't understand. Please try use commands."),
    INVALID_COMMAND(3, "Invalid command use."),
    SUCCESS_COMMAND(4, "Command was used successfully.");

    private final int id;
    @Getter
    private final String text;
}
