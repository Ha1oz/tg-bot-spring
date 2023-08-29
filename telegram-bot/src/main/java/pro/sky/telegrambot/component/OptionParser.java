package pro.sky.telegrambot.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.Notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class OptionParser {
    private static final Logger log = LoggerFactory.getLogger(OptionParser.class);
    public Notification getNotification(Long chatId, String option) {
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
    private LocalDateTime getLocalDateTimeFromString(String str) {
        try {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (RuntimeException e) {
            log.error("Wrong date format");
            return null;
        }
    }
}
