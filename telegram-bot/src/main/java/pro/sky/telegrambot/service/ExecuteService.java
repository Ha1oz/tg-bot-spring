package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class ExecuteService {
    private final TelegramBot telegramBot;

    public ExecuteService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void sendMessage(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }
    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        sendMessage(message);
    }

}
