package com.kalmyk.repository;


import com.kalmyk.service.command.SessionContext;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<SessionContext, String> {
    SessionContext findByChatId(Long chatId);
}