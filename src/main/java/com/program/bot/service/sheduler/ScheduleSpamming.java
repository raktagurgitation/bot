package com.program.bot.service.sheduler;


import com.program.bot.config.BotConfig;
import com.program.bot.repository.PersonRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class ScheduleSpamming {

    private final PersonRepo personRepo;
    private final TelegramClient telegramClient;

    public ScheduleSpamming(PersonRepo personRepo, BotConfig config) {
        this.personRepo = personRepo;
        this.telegramClient = new OkHttpTelegramClient(config.getToken());
    }


    public void sendDailyNotification(){
        personRepo.findAll().forEach(person -> {
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(person.getChatId())
                    .text("Приветик " + person.getFirstName())
                    .build();
            try {
                telegramClient.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        });
    }




}
