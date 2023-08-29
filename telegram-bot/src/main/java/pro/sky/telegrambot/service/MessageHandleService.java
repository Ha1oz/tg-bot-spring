package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.component.OptionParser;
import pro.sky.telegrambot.entity.Notification;

import java.util.Arrays;
import java.util.Optional;

@Service
public class MessageHandleService {
    private static final Logger log = LoggerFactory.getLogger(MessageHandleService.class);
    private final NotificationService notificationService;
    private final OptionParser optionParser;

    public MessageHandleService(NotificationService notificationService, OptionParser optionParser) {
        this.notificationService = notificationService;
        this.optionParser = optionParser;
    }

    public SendMessage handleMessage(Update update) {
        Message message = update.message();

        if (message == null) {
            return null;
        }

        Long chatId = message.chat().id();
        if (message.entities() == null) {
            log.warn("Message doesn't have any command.");
            return new SendMessage(chatId, "I don't understand. Please try use commands.");
        }

        Optional<MessageEntity> commandEntity =
                Arrays.stream(message.entities()).filter(e -> e.type().equals(MessageEntity.Type.bot_command)).findFirst();

        if (commandEntity.isPresent()) {
            String command = message.text().substring(commandEntity.get().offset(), commandEntity.get().length());
            String option = tryFindOptions(message, commandEntity);

            switch (command) {
                case "/notify_me" :

                    Notification notification = optionParser.getNotification(chatId, option);

                    if (notification == null) {
                        log.error("Cannot write notification.");
                        return new SendMessage(chatId, "Invalid command use. " +
                                "Use correct format or set time for notification after now.");
                    }

                    notificationService.saveNotification(notification);
                    break;
                default:
                    log.warn("Unknown command.");
                    return new SendMessage(chatId, "I don't know that command.");
            }
        }
        return new SendMessage(chatId, "Command used successfully.");
    }
    private String tryFindOptions(Message message, Optional<MessageEntity> commandEntity) {
        return message.text().substring(commandEntity.get().length());
    }
}
