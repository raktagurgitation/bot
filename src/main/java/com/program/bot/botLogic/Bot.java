package com.program.bot.botLogic;


import com.program.bot.config.BotConfig;
import com.program.bot.service.MessageHandler;
import com.program.bot.service.PersonService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


import java.util.List;


@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class Bot implements SpringLongPollingBot, LongPollingUpdateConsumer {

    final BotConfig config;
    final MessageHandler messageHandler;
    final PersonService personService;

    public Bot(BotConfig config, MessageHandler messageHandler, PersonService personService) {
        this.config = config;
        this.messageHandler = messageHandler;
        this.personService = personService;
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(List<Update> updates) {
        updates.forEach(update -> {
            System.out.println("работаю");
            if (update.hasMessage()) {
                personService.register(update.getMessage());
            }
            messageHandler.handleCommand(update);
        });

    }

}