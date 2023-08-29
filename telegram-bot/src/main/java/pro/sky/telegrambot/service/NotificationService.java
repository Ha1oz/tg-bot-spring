package pro.sky.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.Notification;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification saveNotification(Notification notification) {
        log.info("Save notification in DB.");
        return notificationRepository.save(notification);
    }
    public Optional<Notification> findNotification(Long id) {
        log.info("Find notification in DB.");
        return notificationRepository.findById(id);
    }
    public void deleteNotification(Long id) {
        log.info("Delete notification in DB.");
        notificationRepository.deleteById(id);
    }
    public List<Notification> findByDateFormat(LocalDateTime localDateTime) {
        log.info("Find-by-date-format notifications in DB.");
        return notificationRepository.findByTime(localDateTime);
    }
    public List<Notification> findAllNotifications(){
        log.info("Find-all notifications in DB.");
        return notificationRepository.findAll();
    }
}
