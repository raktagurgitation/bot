package com.program.bot.service;

import com.program.bot.config.BotConfig;
import com.program.bot.dto.JokeDto;
import com.program.bot.entity.Person;
import com.program.bot.keyboard.ReplyKeyboard;
import com.program.bot.utils.StateType;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.program.bot.utils.BotCommands.*;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageService{

    final TelegramClient telegramClient;
    final NotificationService notificationService;
    final ReplyKeyboard replyKeyboard;
    final PersonService personService;
    final SchedulerService schedulerService;
    final JokeService jokeService;
    final ScheduledExecutorService scheduler;

    public SendMessageService(BotConfig botConfig, NotificationService notificationService, ReplyKeyboard replyKeyboard, PersonService personService, SchedulerService schedulerService, JokeService jokeService, ScheduledExecutorService scheduledExecutorService) {
        this.telegramClient = new OkHttpTelegramClient(botConfig.getToken());
        this.notificationService = notificationService;
        this.replyKeyboard = replyKeyboard;
        this.personService = personService;
        this.schedulerService = schedulerService;
        this.jokeService = jokeService;
        this.scheduler = scheduledExecutorService;
    }

    @SneakyThrows
    public void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        log.info("Sending message with chatID " + chatId);
        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendInfoMessage(long chatId, Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(update.getMessage().getFrom().getFirstName() + ", ты ввел(а) неправильную команду. Вот список доступных:" +
                        START.getCommand() + ", " + HELP.getCommand() + ", " + LOVE.getCommand() + ".")
                .build();
        log.info("Sending message with chatID " + chatId);
        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendMessageWithKeyboardMENU(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard.getReplyKeyboardMarkupMENU())
                .build();
        log.info("Sending message with chatID " + chatId);
        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendMessageAndChangeState(Person person, StateType type, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(person.getChatId())
                .text(text)
                .build();
        personService.changeState(person, type);
        log.info("Sending message with chatID " + person.getChatId());
        telegramClient.execute(message);
    }

    @SneakyThrows
    public void sendMessageWithAllPersonNotifications(Person person) {
        String allNotifications = notificationService.findAllPersonNotifications(person);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(person.getChatId())
                .text(allNotifications)
                .build();
        log.info("Sending info of all notifications with chatID " + person.getChatId());
        telegramClient.execute(sendMessage);
    }

    @SneakyThrows
    @Scheduled(cron = "0 00 11,21 * * *")
    public void sendNotificationByScheduler() {
        List<SendMessage> messages = schedulerService.getActualNotifications();
        if (messages != null) {
            for (SendMessage message : messages) {
                telegramClient.execute(message);
            }
        }
    }

    @SneakyThrows
    public void sendMessageWithKeyboardNOTIFICATION(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard.getReplyKeyboardMarkupNOTIFICATIONS())
                .build();
        telegramClient.execute(sendMessage);
    }


    @SneakyThrows
    public void sendJoke(long chatId) {

        SendChatAction action = new SendChatAction(String.valueOf(chatId), ActionType.TYPING.name());

        telegramClient.execute(action);

        JokeDto joke = jokeService.getJoke();
        SendMessage sendSetup = SendMessage.builder()
                .chatId(chatId)
                .text(joke.getSetup() + " \uD83E\uDD14")
                .build();

        SendMessage sendPunchline = SendMessage.builder()
                .chatId(chatId)
                .text(joke.getPunchline() + " \uD83D\uDE02 \uD83E\uDD23")
                .build();

        telegramClient.execute(sendSetup);
        telegramClient.execute(action);
        scheduler.schedule(()-> {
            try {
                telegramClient.execute(sendPunchline);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        },4, TimeUnit.SECONDS);
    }


}
