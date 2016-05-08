package com.grinno.patients.dao;

import com.grinno.patients.model.User;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);

    public static final String COLLECTION_NAME = "user";

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @param user 
     */
    public void insert(User user) {
        LOGGER.debug("User.insert 1(" + user + ")");

        if (!mongoTemplate.collectionExists(User.class)) {
        LOGGER.debug("User.insert 2");
            mongoTemplate.createCollection(User.class);
        }
        try {
        LOGGER.debug("User.insert 3");
            mongoTemplate.save(user, COLLECTION_NAME);
        LOGGER.debug("User.insert 4");
        }
        catch(Exception ex){
            LOGGER.warn("User insert failed.", ex);
        }
        LOGGER.debug("User.insert 5");
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
        LOGGER.debug("User.findAll 1");
        List<User> users =  mongoTemplate.findAll(User.class, COLLECTION_NAME);
        LOGGER.debug("User.findAll 2(" + users + ")");
        return users;
    }

    public User remove(String id) {
        User user = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), User.class, COLLECTION_NAME);
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
/*        update.set("authorities", user.getAuthorities());
        update.set("locale", user.getLocale());
        update.set("enabled", user.isEnabled());
*/
        mongoTemplate.updateFirst(query, update, User.class);

        return user;
    }
}
