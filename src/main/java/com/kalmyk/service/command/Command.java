package com.kalmyk.service.command;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    String commandDescription();
    String commandName();
    <T extends PartialBotApiMethod> T execute(SessionContext context);
    <T extends PartialBotApiMethod> T process(SessionContext context, Update update);
    boolean isPublic();
}

