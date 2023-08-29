package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.ResponseOption;

@Service
public class MessageExecuteService {
    private final TelegramBot telegramBot;

    public MessageExecuteService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    public void sendMessage(SendMessage message) {
        telegramBot.execute(message);
    }
    public void sendMessage(Long chatId, ResponseOption responseOption) {
        SendMessage message = new SendMessage(chatId, responseOption.getText());
        sendMessage(message);
    }
    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        sendMessage(message);
    }

}
