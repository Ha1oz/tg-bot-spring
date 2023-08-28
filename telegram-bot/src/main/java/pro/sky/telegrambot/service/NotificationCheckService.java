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
    private final ExecuteService executeService;

    public NotificationCheckService(NotificationService notificationService, ExecuteService executeService) {
        this.notificationService = notificationService;
        this.executeService = executeService;
    }
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkNotification() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Check notification with time {} in DB.", now);

        notificationService.findByDateFormat(now).forEach(n -> {
            executeService.sendMessage(new SendMessage(n.getChatId(), n.getText()));
            notificationService.deleteNotification(n.getId());
        });
    }
}
