package com.grinno.patients.dao;

import com.grinno.patients.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 *
 * @author jacek
 */

public interface UserRepository extends MongoRepository<User, String> /*, QueryDslPredicateExecutor<User> */ {

    @Query("{ $and : [ { id: ?0 }, { deleted: { $eq: false } } ] }")    
    User findOneNotDeleted(String id);
}
