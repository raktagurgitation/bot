package com.program.bot.keyboard;



import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.program.bot.utils.BotCommands.*;

@Component
public class ReplyKeyboard {

    public ReplyKeyboardMarkup getReplyKeyboardMarkupMENU() {

        KeyboardRow row1 = new KeyboardRow();
        row1.add(HELP.getCommand());
        row1.add(GET_JOKE.getCommand());
        KeyboardRow row2 = new KeyboardRow();
        row2.add(ABOUT.getCommand());
        row2.add(TO_NOTIFICATIONS.getCommand());

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .inputFieldPlaceholder("Выберите действие...")
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }

    public ReplyKeyboardMarkup getReplyKeyboardMarkupNOTIFICATIONS() {

        KeyboardRow row1 = new KeyboardRow();
        row1.add(NEW_NOTIFICATION.getCommand());
        row1.add(VIEW_NOTIFICATIONS.getCommand());
        KeyboardRow row2 = new KeyboardRow();
        row2.add(DELETE_NOTIFICATIONS.getCommand());
        row2.add(BACK_TO_MENU.getCommand());

        return ReplyKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .inputFieldPlaceholder("Выберите действие...")
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }


}
