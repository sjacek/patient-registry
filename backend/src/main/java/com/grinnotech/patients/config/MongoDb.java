package com.grinnotech.patients.config;

import com.grinnotech.patients.model.CPersistentLogin;
import com.grinnotech.patients.model.CUser;
import com.grinnotech.patients.model.PersistentLogin;
import com.grinnotech.patients.model.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@Component
public class MongoDb {

    private final MongoDatabase mongoDatabase;

    @Autowired
    public MongoDb(final MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    @PostConstruct
    public void createIndexes() {
        if (!indexExists(User.class, CUser.email)) {
            getCollection(User.class).createIndex(Indexes.ascending(CUser.email), new IndexOptions().unique(true));
        }

        if (!indexExists(PersistentLogin.class, CPersistentLogin.userId)) {
            getCollection(PersistentLogin.class).createIndex(Indexes.ascending(CPersistentLogin.userId));
        }
    }

    public boolean indexExists(Class<?> clazz, String indexName) {
        return indexExists(this.getCollection(clazz), indexName);
    }

    public boolean indexExists(MongoCollection<?> collection, String indexName) {
        for (Document doc : collection.listIndexes()) {
            Document key = (Document) doc.get("key");
            if (key != null) {
                if (key.containsKey(indexName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public <T> MongoCollection<T> getCollection(Class<T> documentClass) {
        return getMongoDatabase().getCollection(getCollectionName(documentClass), documentClass);
    }

    private static String getCollectionName(Class<?> documentClass) {
        return StringUtils.uncapitalize(documentClass.getSimpleName());
    }

    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> documentClass) {
        return getMongoDatabase().getCollection(collectionName, documentClass);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return getMongoDatabase().getCollection(collectionName);
    }
}
