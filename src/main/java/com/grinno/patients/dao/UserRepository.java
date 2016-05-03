package com.grinno.patients.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.grinno.patients.model.User;

@Repository
public class UserRepository {

    public static final String COLLECTION_NAME = "user";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @param user
     * @return 
     */
    public User insert(User user) {
        if (!mongoTemplate.collectionExists(User.class)) {
            mongoTemplate.createCollection(User.class);
        }
        mongoTemplate.insert(user, COLLECTION_NAME);
        return user;
    }

    /**
     *
     * @param id
     * @return
     */
    public User findOneById(String id) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(id)), User.class, COLLECTION_NAME);
    }

    /**
     *
     * @param pesel
     * @return
     */
    public User findOneByPesel(String pesel) {
        return mongoTemplate.findOne(
                Query.query(Criteria.where("pesel").is(pesel)), User.class, COLLECTION_NAME);
    }

    /**
     *
     * @return
     */
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class, COLLECTION_NAME);
    }

    public User remove(String id) {
        User user = mongoTemplate.findOne(
                Query.query(Criteria.where("_id").is(id)), User.class, COLLECTION_NAME);
        mongoTemplate.remove(user, COLLECTION_NAME);

        return user;
    }

    public User updateFirst(String id, User user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        Update update = new Update();
        update.set("loginName", user.getLoginName());
        update.set("firstName", user.getFirstName());
        update.set("lastName", user.getLastName());
        update.set("email", user.getEmail());
        update.set("authorities", user.getAuthorities());
        update.set("locale", user.getLocale());
        update.set("enabled", user.isEnabled());

        mongoTemplate.updateFirst(query, update, User.class);

        return user;
    }
}
