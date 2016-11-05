package com.grinno.patients.dao;

import com.grinno.patients.model.User;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String COLLECTION_NAME = "user";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @param user 
     */
    public void save(User user) {
        LOGGER.debug("UserRepository.insert(" + user + ")");

        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }
        try {
            mongoTemplate.save(user, COLLECTION_NAME);
        }
        catch(Exception ex){
            LOGGER.warn("User insert failed.", ex);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public User findOneById(String id) {
        return mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), User.class, COLLECTION_NAME);
    }

    /**
     *
     * @param pesel
     * @return
     */
    public User findOneByPesel(String pesel) {
        return mongoTemplate.findOne(Query.query(Criteria.where("pesel").is(pesel)), User.class, COLLECTION_NAME);
    }

    /**
     *
     * @return
     */
    public List<User> findAll() {
        List<User> users =  mongoTemplate.findAll(User.class, COLLECTION_NAME);
        LOGGER.debug("UserRepository.findAll():" + users);
        return users;
    }

    public User remove(String id) {
        LOGGER.debug("UserRepository.remove(" + id + ")");
        User user = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), User.class, COLLECTION_NAME);
        LOGGER.debug("UserRepository.remove(" + user + ")");
        mongoTemplate.remove(user, COLLECTION_NAME);

        return user;
    }

    public User updateFirst(String id, User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        Update update = new Update();
//        update.set("loginName", user.getLoginName());
        update.set("firstName", user.getFirstName());
        update.set("lastName", user.getLastName());
        update.set("email", user.getEmail());
        update.set("authorities", user.getAuthorities());
        update.set("passwordHash", user.getPasswordHash());
//        update.set("locale", user.getLocale());
//        update.set("enabled", user.isEnabled());
        mongoTemplate.updateFirst(query, update, User.class);

        return user;
    }
}
