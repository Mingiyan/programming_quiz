package com.kalmyk.service.command;

import lombok.Data;
import com.kalmyk.model.Question;

import java.util.ArrayList;
import java.util.List;

@Data
public class QuizContext {
    private List<Question> askedQuestions = new ArrayList<>();
    private int correctAnswers;
    private int quizSize = 10;
}