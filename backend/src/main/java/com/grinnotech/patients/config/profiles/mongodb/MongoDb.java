package com.grinnotech.patients.config.profiles.mongodb;

import com.grinnotech.patients.model.CPersistentLogin;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.PersistentLogin;
import com.grinnotech.patients.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

import static com.mongodb.client.model.Indexes.ascending;
import static com.mongodb.client.model.Indexes.compoundIndex;

@Component
@Profile("mongodb")
public class MongoDb {

    @Autowired
    private MongoDatabase mongoDatabase;

    private static String getCollectionName(Class<?> documentClass) {
        return StringUtils.uncapitalize(documentClass.getSimpleName());
    }

    @PostConstruct
    public void createIndexes() {
        if (!indexExists(User.class, CUser.email)) {
            getCollection(User.class).createIndex(ascending(CUser.email, "_version"), new IndexOptions().unique(true));
        }

        if (!indexExists(PersistentLogin.class, CPersistentLogin.userId)) {
            getCollection(PersistentLogin.class).createIndex(ascending(CPersistentLogin.userId));
        }
    }

    private boolean indexExists(Class<?> clazz, String indexName) {
        return indexExists(getCollection(clazz), indexName);
    }

//    private MongoDatabase getMongoDatabase() {
//        return mongoDatabase;
//    }

    private boolean indexExists(MongoCollection<?> collection, String indexName) {
        for (Document doc : collection.listIndexes()) {
            if (doc.containsKey("key")) {
                Document key = (Document) doc.get("key");

                if (key.containsKey(indexName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public <T> MongoCollection<T> getCollection(Class<T> documentClass) {
        return mongoDatabase.getCollection(getCollectionName(documentClass), documentClass);
    }

    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> documentClass) {
        return mongoDatabase.getCollection(collectionName, documentClass);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return mongoDatabase.getCollection(collectionName);
    }
}
