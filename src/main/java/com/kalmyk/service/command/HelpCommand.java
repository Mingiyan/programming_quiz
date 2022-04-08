package com.kalmyk.service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final Set<Command> allCommands;

    @Override
    public String commandDescription() {
        return "Get list of commands";
    }

    @Override
    public String commandName() {
        return "/help";
    }

    @Override
    public PartialBotApiMethod execute(SessionContext context) {
        return null;
    }

    @Override
    public PartialBotApiMethod process(SessionContext context, Update update) {
        throw new UnsupportedOperationException("Help command is not supported 'process' method");
    }

    @Override
    public boolean isPublic() {
        return true;
    }

    private String getCommandDisplayName(Command command) {
        return command.commandName() + " -- " + command.commandDescription();
    }
}
