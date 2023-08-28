package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.MessageHandleService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class BotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(BotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private MessageHandleService messageHandleService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

           SendMessage answer = messageHandleService.handleMessage(update);
           if (answer != null) {
               telegramBot.execute(answer);
           }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



}
