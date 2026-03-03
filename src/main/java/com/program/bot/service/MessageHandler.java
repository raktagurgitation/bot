package com.program.bot.service;


import com.program.bot.entity.Person;
import com.program.bot.service.sheduler.ScheduleSpamming;
import com.program.bot.utils.BotCommands;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.program.bot.utils.BotCommands.*;
import static com.program.bot.utils.StateType.*;


@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageHandler {

    final ScheduleSpamming scheduleSpamming;
    final NotificationService notificationService;
    final PersonService personService;
    final SendMessageService sendService;


    public MessageHandler(ScheduleSpamming scheduleSpamming, NotificationService notificationService, PersonService personService, SendMessageService sendService) {
        this.scheduleSpamming = scheduleSpamming;
        this.notificationService = notificationService;
        this.personService = personService;
        this.sendService = sendService;
    }

    public void handleCommand(Update update) {

        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        Person person = personService.findById(chatId);

        // Режим ввода названия события для создания
        if (person.getState().equals(WAITING_NAME.name())) {
            notificationService.setNotificationName(person, messageText);
            sendService.sendMessageAndChangeState(person, WAITING_DATA,
                    "Отлично, а теперь введите дату. Формат должен быть следующий: 0000 00 00. Например - 2026 05 19");
            return;
        }

        // Режим ввода даты события
        else if (person.getState().equals(WAITING_DATA.name())) {
            String check = checkDate(messageText);
            if (check != null) {
                sendService.sendMessage(person.getChatId(), check);
            } else {
                notificationService.setNotificationDate(person, messageText);
                sendService.sendMessageAndChangeState(person, NORMAL, "Вы успешно установили уведомление на событие!");
            }
            return;
        }

        // Режим ввода названия события для удаления
        else if (person.getState().equals(WAITING_DELETE.name())) {
            String result = notificationService.deleteByName(person, messageText);
            if (result != null) {
                sendService.sendMessage(person.getChatId(), result);

            } else {
                sendService.sendMessageAndChangeState(person, NORMAL, "Событие успешно удалено");
            }
            return;
        }

        // Нормальный режим
        if (checkMessageIsCommand(messageText) && person.getState().equals(NORMAL.name())) {
            // Блок MENU
            if (messageText.equals(START.getCommand())) {
                sendService.sendMessageWithKeyboardMENU(chatId,
                        """
                                Отлично. А теперь давай создадим твое 1 событие. Перейди во вкладку события и нажми создать :)
                                """);
                return;
            }
            if (messageText.equals(HELP.getCommand())) {
                sendService.sendMessage(chatId, "Я поддерживаю следующие команды: " +
                        START.getCommand() + ", " + HELP.getCommand() + ", " + LOVE.getCommand() + ", " + NEW_NOTIFICATION_INLINE.getCommand() + ".");
                return;
            }
//            if (messageText.equals(LOVE.getCommand())) {
//                sendService.sendMessage(chatId, "Люблю Владочку :)");
//                return;
//            }
            if (messageText.equals(ABOUT.getCommand())) {
                sendService.sendMessage(chatId, "Запуск 23.02.2026. Функционал находится в разработке.");
                return;
            }
            if (messageText.equals(TO_NOTIFICATIONS.getCommand())) {
                sendService.sendMessageWithKeyboardNOTIFICATION(chatId, "Вы перешли в режим работы с событиями.");
                return;
            }
            if (messageText.equals(GET_JOKE.getCommand())) {
                sendService.sendJoke(chatId);
                return;
            }

            // Блок NOTIFICATION
            if (messageText.equals(BACK_TO_MENU.getCommand())) {
                sendService.sendMessageWithKeyboardMENU(chatId, "Вы вернулись в меню");
                return;
            }
            if (messageText.equals(NEW_NOTIFICATION.getCommand()) || messageText.equals(NEW_NOTIFICATION_INLINE.getCommand())) {
                sendService.sendMessageAndChangeState(person, WAITING_NAME, "Вы приступили к созданию события! Введите название.");
                return;
            }
            if (messageText.equals(VIEW_NOTIFICATIONS.getCommand())) {
                sendService.sendMessageWithAllPersonNotifications(person);
                return;
            }
            if (messageText.equals(DELETE_NOTIFICATIONS.getCommand())) {
                sendService.sendMessageAndChangeState(person, WAITING_DELETE, "Введите название события для его удаления.");
                return;
            }

        } else {
            // Если нет никакой команды
            if (person.getState().equals(NORMAL.name())) {
                sendService.sendInfoMessage(chatId, update);
                return;
            }
        }
    }


    public String checkDate(String date) {
        String regex = "^\\d{4}\\s(0[1-9]|1[0-2])\\s(0[1-9]|1[0-9]|2[0-9]|3[0-1])$";
        if (!date.matches(regex)) {
            log.error("Введенная дата неверна!");
            return "Дата введена некорректно. Попробуй еще раз. Пример: 2026 05 19";
        }
        return null;
    }

    private boolean checkMessageIsCommand(String text) {

        for (int i = 0; i < BotCommands.values().length; i++) {
            if (BotCommands.values()[i].getCommand().equals(text)) {
                return true;
            }
        }

        return false;
    }

    @Scheduled(cron = "0 0 20 * * *")
    private void sendDailyNotification() {
        scheduleSpamming.sendDailyNotification();
    }

}
