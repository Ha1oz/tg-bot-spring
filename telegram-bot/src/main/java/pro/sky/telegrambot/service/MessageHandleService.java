package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.component.OptionParser;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.entity.ResponseOption;

import java.util.Arrays;
import java.util.Optional;

@Service
public class MessageHandleService {
    private static final Logger log = LoggerFactory.getLogger(MessageHandleService.class);
    private final NotificationService notificationService;
    private final OptionParser optionParser;
    private final MessageExecuteService messageExecuteService;

    public MessageHandleService(NotificationService notificationService, OptionParser optionParser, MessageExecuteService messageExecuteService) {
        this.notificationService = notificationService;
        this.optionParser = optionParser;
        this.messageExecuteService = messageExecuteService;
    }

    public void handleMessage(Update update) {
        Message message = update.message();

        if (message == null) {
            return;
        }

        Long chatId = message.chat().id();
        if (message.entities() == null) {
            log.warn("Message doesn't have any command.");
            messageExecuteService.sendMessage(chatId,ResponseOption.ONLY_COMMANDS);
            return;
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
                        messageExecuteService.sendMessage(chatId, ResponseOption.INVALID_COMMAND);
                        return;
                    }

                    notificationService.saveNotification(notification);
                    break;
                default:
                    log.warn("Unknown command.");
                    messageExecuteService.sendMessage(chatId, ResponseOption.UNKNOWN_COMMAND);
                    return;
            }
        }
        messageExecuteService.sendMessage(chatId, ResponseOption.SUCCESS_COMMAND);
    }
    private String tryFindOptions(Message message, Optional<MessageEntity> commandEntity) {
        return message.text().substring(commandEntity.get().length());
    }
}
