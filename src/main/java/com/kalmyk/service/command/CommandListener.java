package com.kalmyk.service.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CommandListener {


    public CommandListener() {

    }

    public BotApiMethod getCommand(Update update) {
        BotApiMethod botApiMethod = null;

        return botApiMethod;
    }
}
