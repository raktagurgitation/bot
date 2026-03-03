package com.program.bot.service;

import com.program.bot.entity.Notification;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SchedulerService {


    private final NotificationService notificationService;

    public SchedulerService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private List<Notification> checkDatabase(){
        log.info("Начал проверку даты.");
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        return notificationService.findAllByNotificationDate(tomorrow);
    }

    @Transactional
    public List<SendMessage> getActualNotifications(){
        Long chatId;
        List<Notification> notifications = checkDatabase();
        if (notifications!=null){
            List<SendMessage> messages = new ArrayList<>();
            SendMessage sendMessage = null;
            for(Notification notification:notifications){
                chatId = notification.getPerson().getChatId();
                sendMessage = SendMessage.builder()
                        .chatId(chatId)
                        .text("Привет, твое событие " + notification.getName() + " уже завтра! Не пропусти его.")
                        .build();
                messages.add(sendMessage);
            }
            return messages;
        }
        else {
            return null;
        }
    }
}
