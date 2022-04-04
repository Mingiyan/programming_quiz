package com.kalmyk.config;

import com.kalmyk.bot.ProgrammingQuizBot;
import com.kalmyk.service.command.Command;
import com.kalmyk.service.command.CommandListener;
import com.kalmyk.service.command.CommandType;
import com.kalmyk.service.command.StartQuizCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.ApiContext;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class BotConfig {

    @Value("${BOT_TOKEN}")
    private String botToken;

    @Value("${BOT_USERNAME}")
    private String botUserName;

    @Value("${BOT_BASE_URL}")
    private String botBaseUrl;

    private final CommandListener commandListener;

    public BotConfig(CommandListener commandListener) {
        this.commandListener = commandListener;
    }


    @Bean
    public TelegramLongPollingBot telegramLongPollingBot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        if (botBaseUrl != null && !botBaseUrl.isEmpty()) {
            options.setBaseUrl(botBaseUrl);
        }
        return new ProgrammingQuizBot(
                options,
                botToken, botUserName, commandListener);
    }

    @Bean
    public Map<CommandType, Command> activeCommandHashMap(StartQuizCommand startQuizCommand) {
        Map<CommandType, Command> activeCommandHashMap = new HashMap<>();
        activeCommandHashMap.put(CommandType.START_QUIZ, startQuizCommand);
        return activeCommandHashMap;
    }
}
