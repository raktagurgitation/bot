package com.program.bot.utils;


import lombok.Getter;


@Getter
public enum BotCommands {

    START("/start"),
    HELP("/help"),
    LOVE("️/love"),
    ABOUT("/about"),
    BACK_TO_MENU("В меню \uD83D\uDD19"),
    TO_NOTIFICATIONS("К событиям ✉️"),
    NEW_NOTIFICATION("Создать \uD83D\uDCDD"),
    NEW_NOTIFICATION_INLINE("/new_event"),
    VIEW_NOTIFICATIONS("Просмотр моих событий \uD83D\uDD0E"),
    DELETE_NOTIFICATIONS("Удалить \uD83D\uDDD1\uFE0F");




    private final String command;

    BotCommands(String command) {
        this.command = command;
    }

}
