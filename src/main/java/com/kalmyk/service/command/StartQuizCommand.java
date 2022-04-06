package com.kalmyk.service.command;

import com.kalmyk.model.Question;
import com.kalmyk.model.Tag;
import com.kalmyk.service.QuestionService;
import com.kalmyk.service.button.AnswerButtonsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
@RequiredArgsConstructor
public class StartQuizCommand implements Command {

    private final QuestionService questionService;
    private final AnswerButtonsService answerButtonsService;

    @Override
    public String commandDescription() {
        return "Start quiz";
    }

    @Override
    public String commandName() {
        return "/startQuiz";
    }

    @Override
    public BotApiMethod execute(SessionContext context) {
        QuizContext quizContext = new QuizContext();
        context.setQuizContext(quizContext);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startQuiz(quizContext);
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        return new SendMessage(context.getChatId(), "1. " + question.getQuestion()).setReplyMarkup(answerButtons);
    }

    public BotApiMethod executeWithTag(SessionContext context, String tag) {
        QuizContext quizContextTag = new QuizContext();
        context.setQuizContext(quizContextTag);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startWithTag(quizContextTag, new Tag(tag));
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        return new SendMessage(context.getChatId(), "1. " + question.getQuestion()).setReplyMarkup(answerButtons);
    }

    @Override
    public BotApiMethod process(SessionContext context, Update update) {
        return null;
    }

    @Override
    public boolean isPublic() {
        return false;
    }
}
