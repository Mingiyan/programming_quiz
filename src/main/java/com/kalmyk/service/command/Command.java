package com.kalmyk.service.command;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String commandDescription();
    String commandName();
    PartialBotApiMethod execute(SessionContext context);
    PartialBotApiMethod process(SessionContext context, Update update);
    boolean isPublic();
}
