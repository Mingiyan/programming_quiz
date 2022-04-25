package com.kalmyk.service.command;

import com.kalmyk.model.Question;
import com.kalmyk.model.Tag;
import com.kalmyk.service.QuestionService;
import com.kalmyk.service.button.AnswerButtonsService;
import com.kalmyk.service.utils.TelegramUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StartQuizCommand implements Command {

    private final QuestionService questionService;
    private final AnswerButtonsService answerButtonsService;
    private final TelegramUtils telegramUtils;

    public StartQuizCommand(QuestionService questionService,
                            AnswerButtonsService answerButtonsService,
                            TelegramUtils telegramUtils) {
        this.questionService = questionService;
        this.answerButtonsService = answerButtonsService;
        this.telegramUtils = telegramUtils;
    }

    @Override
    public String commandDescription() {
        return "Start quiz";
    }

    @Override
    public String commandName() {
        return "/startQuiz";
    }

    @Override
    public PartialBotApiMethod execute(SessionContext context) {
        QuizContext quizContext = new QuizContext();
        context.setQuizContext(quizContext);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startQuiz(quizContext);
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        if (question.getIsImage()) {
            return new SendPhoto()
                    .setChatId(context.getChatId())
                    .setCaption("1. ")
                    .setPhoto(question.getQuestion())
                    .setReplyMarkup(answerButtons);
        } else {
            return new SendMessage(context.getChatId(), "1. " + question.getQuestion()).setReplyMarkup(answerButtons);
        }
    }

    public PartialBotApiMethod executeWithTag(SessionContext context, String tag) {
        QuizContext quizContextTag = new QuizContext();
        context.setQuizContext(quizContextTag);
        context.setActiveCommand(CommandType.START_QUIZ);
        Question question = questionService.startWithTag(quizContextTag, new Tag(tag));
        InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
        if (question.getIsImage()) {
            return new SendPhoto()
                    .setChatId(context.getChatId())
                    .setCaption("1. ")
                    .setPhoto(question.getQuestion())
                    .setReplyMarkup(answerButtons);
        } else {
            return new SendMessage(context.getChatId(), "1. " + question.getQuestion()).setReplyMarkup(answerButtons);
        }
    }

    @Override
    public PartialBotApiMethod process(SessionContext context, Update update) {
        Message incomingMsg = telegramUtils.getMessage(update);

        PartialBotApiMethod method;
        Question question = questionService.processQuiz(context.getQuizContext(), update.getCallbackQuery());

        if (question != null) {
            String correctAnswer = context.getQuizContext().getAskedQuestions()
                    .get(context.getQuizContext().getAskedQuestions().size() - 2).getCorrectAnswer().getAnswer();
            boolean isCorrect = update.getCallbackQuery().getData().equalsIgnoreCase(correctAnswer);
            String message;
            if (isCorrect) {
                message = "\u2705 Yes!";
            } else {
                message = "\u274c No!";
            }
            InlineKeyboardMarkup answerButtons = answerButtonsService.buildQuestion(question);
            if (question.getIsImage()) {
                method = new SendPhoto()
                        .setChatId(context.getChatId())
                        .setCaption(context.getQuizContext().getAskedQuestions().size() + ". ")
                        .setPhoto(question.getQuestion())
                        .setReplyMarkup(answerButtons);
            } else {
                method = new SendMessage(context.getChatId(),
                        context.getQuizContext().getAskedQuestions().size() + ". " + question.getQuestion()).setReplyMarkup(answerButtons);
            }
        } else {
            String correctAnswer = context.getQuizContext().getAskedQuestions()
                    .get(context.getQuizContext().getAskedQuestions().size() - 1).getCorrectAnswer().getAnswer();
            boolean isCorrect = update.getCallbackQuery().getData().equalsIgnoreCase(correctAnswer);
            String message;
            if (isCorrect) {
                message = "\u2705 Yes!";
            } else {
                message = "\u274c No!";
            }
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            List<InlineKeyboardButton> button = new ArrayList<>();
            InlineKeyboardButton buttonBack = new InlineKeyboardButton();
            buttonBack.setText("Back").setCallbackData("/help");
            button.add(buttonBack);
            inlineKeyboardMarkup.setKeyboard(Collections.singletonList(button));
            SendMessage sendMessage = new SendMessage(incomingMsg.getChatId(),
                    "Correct answers " + context.getQuizContext().getCorrectAnswers() + "/" +
                            context.getQuizContext().getAskedQuestions().size() + "\n").setReplyMarkup(inlineKeyboardMarkup);
            context.setQuizContext(null);
            context.setActiveCommand(null);
            method = sendMessage;
        }

        return method;
    }

    @Override
    public boolean isPublic() {
        return true;
    }
}
