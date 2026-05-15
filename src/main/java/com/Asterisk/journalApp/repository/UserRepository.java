package com.Asterisk.journalApp.repository;

import com.Asterisk.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, ObjectId> {

    User findByUsername(String username);

    void deleteByUsername(String username);

    @Query("{sentimentAnalysis: true}")
    List<User> getUserForSA();
}
