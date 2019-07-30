package com.grinnotech.patients.mongodb.daos;


import com.grinnotech.patients.mongodb.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, Long> {

}
