package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class NotificationCheckService {
    private static final Logger log = LoggerFactory.getLogger(NotificationCheckService.class);
    private final NotificationService notificationService;
    private final MessageExecuteService messageExecuteService;

    public NotificationCheckService(NotificationService notificationService, MessageExecuteService messageExecuteService) {
        this.notificationService = notificationService;
        this.messageExecuteService = messageExecuteService;
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkNotification() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Check notification with time {} in DB.", now);

        notificationService.findByDateFormat(now).forEach(n -> {
            messageExecuteService.sendMessage(new SendMessage(n.getChatId(), n.getText()));
            notificationService.deleteNotification(n.getId());
        });
    }
}
