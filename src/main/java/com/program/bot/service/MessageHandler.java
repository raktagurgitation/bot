package com.program.bot.service;


import com.program.bot.entity.Person;
import com.program.bot.service.sheduler.ScheduleSpamming;
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

        // Блок работы с CallbackQuery
        if (update.hasCallbackQuery()) {

            // Получаем данные для работы
            String callbackQuery = update.getCallbackQuery().getData();
            String queryId = update.getCallbackQuery().getId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            Person person = personService.findById(chatId);


            if (callbackQuery.equals(GET_JOKE.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendJoke(chatId);
                return;
            }
            if (callbackQuery.equals(HELP.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageBackToMenuButton(chatId, """
                        Для того, что бы создать событие, нажми в меню кнопку 'К событиям'. Затем создай новое событие.
                        Для того, что бы получить шутку нажми в меню 'Шутка'.
                        """);
                return;
            }
            if (callbackQuery.equals(ABOUT.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageBackToMenuButton(chatId, "Запуск 23.02.2026. Функционал находится в разработке.");
                return;
            }
            if (callbackQuery.equals(TO_NOTIFICATIONS.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageWithKeyboardNOTIFICATION(chatId, "Вы перешли в режим работы с событиями.");
                return;
            }
            if (callbackQuery.equals(BACK_TO_MENU.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageWithKeyboardMENU(chatId, "Вы вернулись в меню. Выберите действие");
                return;
            }
            if (callbackQuery.equals(NEW_NOTIFICATION.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageAndChangeState(person, WAITING_NAME, "Вы приступили к созданию события! Введите название.");
                return;
            }
            if (callbackQuery.equals(VIEW_NOTIFICATIONS.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageWithAllPersonNotifications(person);
                return;
            }
            if (callbackQuery.equals(DELETE_NOTIFICATIONS.getCommand())) {
                sendService.answerCallback(queryId);
                sendService.sendMessageAndChangeState(person, WAITING_DELETE, "Введите название события для его удаления.");
                return;
            }
        } else if (update.hasMessage()) {

            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Person person = personService.findById(chatId);

            if (messageText.equals(START.getCommand())) {
                sendService.sendMessageWithKeyboardMENU(chatId,
                        """
                                Ну что же, давай начнем!
                                Выбери пункт ниже, что ты хочешь сделать.
                                """);
                return;
            }
            // Режим ввода названия события для создания
            if (person.getState().equals(WAITING_NAME.name())) {
                notificationService.setNotificationName(person, messageText);
                sendService.sendMessageAndChangeState(person, WAITING_DATA,
                        "Отлично, а теперь введите дату. Формат должен быть следующий: 0000 00 00. Например - 2026 05 19");
                return;
            } else if (person.getState().equals(WAITING_DATA.name())) {
                String check = checkDate(messageText);
                if (check != null) {
                    sendService.sendMessage(person.getChatId(), check);
                } else {
                    notificationService.setNotificationDate(person, messageText);
                    sendService.sendMessageAndChangeStateWithBackButton(person, NORMAL, "Вы успешно установили уведомление на событие!");
                }
                return;
            } else if (person.getState().equals(WAITING_DELETE.name())) {
                String result = notificationService.deleteByName(person, messageText);
                if (result != null) {
                    sendService.sendMessage(person.getChatId(), result);

                } else {
                    sendService.sendMessageAndChangeStateWithBackButton(person, NORMAL, "Событие успешно удалено");
                }
                return;
            } else if (person.getState().equals(NORMAL.name())) {
                // Если нет никакой команды
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

    @Scheduled(cron = "0 33 22 * * *")
    private void sendDailyNotification() {
        scheduleSpamming.sendDailyNotification();
    }

}
