package com.kalmyk.repository;

import com.kalmyk.model.Question;
import com.kalmyk.model.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question, String> {
    List<Question> findAllByTags(Tag tag);
}
