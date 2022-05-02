package com.kalmyk.service.command;

import com.kalmyk.repository.SessionRepository;
import com.kalmyk.service.utils.TelegramUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class CommandListener {

    private final HelpCommand helpCommand;
    private final StartQuizCommand startQuizCommand;
    private final SessionRepository sessionRepository;
    private final TelegramUtils telegramUtils;
    private final Map<CommandType, Command> activeCommandMap;

    public CommandListener(HelpCommand helpCommand,
                           StartQuizCommand startQuizCommand,
                           SessionRepository sessionRepository,
                           TelegramUtils telegramUtils,
                           Map<CommandType, Command> activeCommandMap) {
        this.helpCommand = helpCommand;
        this.startQuizCommand = startQuizCommand;
        this.sessionRepository = sessionRepository;
        this.telegramUtils = telegramUtils;
        this.activeCommandMap = activeCommandMap;
    }

    public BotApiMethod getCommand(Update update) {
        Message incomingMsg = telegramUtils.getMessage(update);

        SessionContext context = sessionRepository.findByChatId(incomingMsg.getChatId());
        if (context == null) {
            context = new SessionContext();
            context.setChatId(incomingMsg.getChatId());
            sessionRepository.save(context);
        }

        BotApiMethod botApiMethod;

        if (update.hasMessage()) {
            botApiMethod = (BotApiMethod) helpCommand.execute(context);
        } else {
            switch (update.getCallbackQuery().getData()) {
                case "/help":
                    botApiMethod = (BotApiMethod) helpCommand.execute(context);
                    break;
                case "/startQuiz":
                    botApiMethod = (BotApiMethod) startQuizCommand.execute(context);
                    break;
                case "/startJavaQuiz":
                    botApiMethod = (BotApiMethod) startQuizCommand.executeWithTag(context, "java");
                    break;
                case "/startPythonQuiz":
                    botApiMethod = (BotApiMethod) startQuizCommand.executeWithTag(context, "python");
                    break;
                case "/startSqlQuiz":
                    botApiMethod = (BotApiMethod) startQuizCommand.executeWithTag(context, "sql");
                    break;
                default:
                    if (context.getActiveCommand() != null) {
                        botApiMethod = activeCommandMap.get(context.getActiveCommand()).process(context, update);
                    } else {
                        botApiMethod = (BotApiMethod) helpCommand.execute(context);
                    }
            }
        }
        sessionRepository.save(context);
        return botApiMethod;
    }
}
