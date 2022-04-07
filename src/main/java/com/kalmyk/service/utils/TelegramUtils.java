package com.kalmyk.service.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TelegramUtils {

    public Message getMessage(Update update) {
        if (!update.hasMessage()) {
            return update.getCallbackQuery().getMessage();
        } else {
            return update.getMessage();
        }
    }
}
