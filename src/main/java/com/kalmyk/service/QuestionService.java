package com.kalmyk.service;

import com.kalmyk.model.Question;
import com.kalmyk.model.Tag;
import com.kalmyk.repository.QuestionRepository;
import com.kalmyk.service.command.QuizContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class QuestionService {

    private QuestionRepository questionRepository;
    private List<Question> list = new ArrayList<>();

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Question startQuiz(QuizContext quizContext) {
        list = questionRepository.findAll();
        Question question = getRandomQuestion();
        quizContext.getAskedQuestions().add(question);
        return question;
    }

    public Question startWithTag(QuizContext quizContext, Tag tag) {
        list = questionRepository.findAllByTags(tag);
        Question question = getRandomQuestion();
        quizContext.getAskedQuestions().add(question);
        return question;
    }

    private Question getRandomQuestion() {
        Random random = new Random();
        List<Question> questionList = list;
        int number = random.nextInt(questionList.size());
        Question question = questionList.get(number);
        questionList.remove(number);
        return question;
    }
}
