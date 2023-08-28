package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MessageHandleService {
    private static final Logger log = LoggerFactory.getLogger(MessageHandleService.class);
    private final NotificationService notificationService;

    public MessageHandleService(NotificationService notificationService) {
        this.notificationService = notificationService;
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

                    Notification notification = getNotificationFromOption(option,
                            update.message().chat().id());

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

    private LocalDateTime getLocalDateTimeFromString(String str) {
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (RuntimeException e) {
            log.error("Wrong date format");
            return null;
        }
    }

    private Notification getNotificationFromOption(String option, Long chatId) {
        String[] params = option.split(" ");

        if (params.length < 4) {
            log.error("Invalid amount of parameters.");
            return null;
        }
        Notification notification = new Notification();

        LocalDateTime time = getLocalDateTimeFromString(params[1] + " " + params[2]);
        if (time == null || time.isBefore(LocalDateTime.now())) {
            return null;
        }

        notification.setTime(time);

        StringBuilder sb = new StringBuilder();
        for (int i = 3; i < params.length; i++) {
            sb.append(params[i]).append(" ");
        }

        notification.setText(sb.toString());
        notification.setChatId(chatId);

        return notification;
    }
    private String tryFindOptions(Message message, Optional<MessageEntity> commandEntity) {
        try {
            return message.text().substring(commandEntity.get().length());
        }
        catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
