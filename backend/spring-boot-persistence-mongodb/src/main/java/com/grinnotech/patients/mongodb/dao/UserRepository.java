package com.grinnotech.patients.mongodb.dao;


import com.grinnotech.patients.mongodb.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Long> {

}
