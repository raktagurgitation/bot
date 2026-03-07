package com.program.bot.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;


import static com.program.bot.utils.BotCommands.*;


@Component
public class InlineKeyboard {

    public InlineKeyboardMarkup getNextJokeKeyboardMarkup() {

        InlineKeyboardButton nextJokeButton = InlineKeyboardButton.builder()
                .text("Следующая шутка")
                .callbackData(GET_JOKE.getCommand())
                .build();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Назад в меню")
                .callbackData(BACK_TO_MENU.getCommand())
                .build();

        InlineKeyboardRow buttonRow1 = new InlineKeyboardRow();
        buttonRow1.add(nextJokeButton);

        InlineKeyboardRow buttonRow2 = new InlineKeyboardRow();
        buttonRow2.add(backButton);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(buttonRow1)
                .keyboardRow(buttonRow2)
                .build();
    }

    public InlineKeyboardMarkup getMenuListKeyboardMarkup(){

        InlineKeyboardButton helpButton = InlineKeyboardButton.builder()
                .text("Помощь")
                .callbackData(HELP.getCommand())
                .build();
        InlineKeyboardButton aboutButton = InlineKeyboardButton.builder()
                .text("О боте")
                .callbackData(ABOUT.getCommand())
                .build();
        InlineKeyboardButton toNotificationButton = InlineKeyboardButton.builder()
                .text("Перейти к событиям")
                .callbackData(TO_NOTIFICATIONS.getCommand())
                .build();
        InlineKeyboardButton jokeButton = InlineKeyboardButton.builder()
                .text("Шутка")
                .callbackData(GET_JOKE.getCommand())
                .build();
        InlineKeyboardRow row1 = new InlineKeyboardRow();
        row1.add(helpButton);
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(aboutButton);
        InlineKeyboardRow row3 = new InlineKeyboardRow();
        row3.add(toNotificationButton);
        InlineKeyboardRow row4 = new InlineKeyboardRow();
        row4.add(jokeButton);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .keyboardRow(row3)
                .keyboardRow(row4)
                .build();

    }

    public InlineKeyboardMarkup backToMenuKeyboardMarkup(){
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Назад в меню")
                .callbackData(BACK_TO_MENU.getCommand())
                .build();
        InlineKeyboardRow row = new  InlineKeyboardRow();
        row.add(backButton);
        return InlineKeyboardMarkup.builder()
                .keyboardRow(row)
                .build();
    }

    public InlineKeyboardMarkup getNotificationMenuKeyboardMarkup(){
        InlineKeyboardButton newNotification = InlineKeyboardButton.builder()
                .text("Создать событие")
                .callbackData(NEW_NOTIFICATION.getCommand())
                .build();
        InlineKeyboardButton viewNotification = InlineKeyboardButton.builder()
                .text("Просмотр событий")
                .callbackData(VIEW_NOTIFICATIONS.getCommand())
                .build();
        InlineKeyboardButton deleteNotification = InlineKeyboardButton.builder()
                .text("Удалить событие")
                .callbackData(DELETE_NOTIFICATIONS.getCommand())
                .build();
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Назад в меню")
                .callbackData(BACK_TO_MENU.getCommand())
                .build();
        InlineKeyboardRow row1 = new InlineKeyboardRow();
        row1.add(newNotification);
        InlineKeyboardRow row2 = new InlineKeyboardRow();
        row2.add(viewNotification);
        InlineKeyboardRow row3 = new InlineKeyboardRow();
        row3.add(deleteNotification);
        InlineKeyboardRow row4 = new InlineKeyboardRow();
        row4.add(backButton);

        return InlineKeyboardMarkup.builder()
                .keyboardRow(row1)
                .keyboardRow(row2)
                .keyboardRow(row3)
                .keyboardRow(row4)
                .build();
    }

    public InlineKeyboardMarkup backToNotificationsKeyboardMarkup(){
        InlineKeyboardButton backButton = InlineKeyboardButton.builder()
                .text("Назад к событиям")
                .callbackData(TO_NOTIFICATIONS.getCommand())
                .build();
        InlineKeyboardRow row = new  InlineKeyboardRow();
        row.add(backButton);
        return InlineKeyboardMarkup.builder()
                .keyboardRow(row)
                .build();
    }




}
