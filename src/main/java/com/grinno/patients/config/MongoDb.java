package com.grinno.patients.config;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

import com.grinno.patients.model.CPersistentLogin;
import com.grinno.patients.model.CUser;
import com.grinno.patients.model.PersistentLogin;
import com.grinno.patients.model.User;

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
            this.getCollection(User.class).createIndex(Indexes.ascending(CUser.email),
                    new IndexOptions().unique(true));
        }

        if (!indexExists(PersistentLogin.class, CPersistentLogin.userId)) {
            this.getCollection(PersistentLogin.class)
                    .createIndex(Indexes.ascending(CPersistentLogin.userId));
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

    public boolean collectionExists(final Class<?> clazz) {
        return collectionExists(getCollectionName(clazz));
    }

    public boolean collectionExists(final String collectionName) {
        return this.getMongoDatabase().listCollections()
                .filter(Filters.eq("name", collectionName)).first() != null;
    }

    public MongoDatabase getMongoDatabase() {
        return this.mongoDatabase;
    }

    public <T> MongoCollection<T> getCollection(Class<T> documentClass) {
        return this.getMongoDatabase().getCollection(getCollectionName(documentClass),
                documentClass);
    }

    private static String getCollectionName(Class<?> documentClass) {
        return StringUtils.uncapitalize(documentClass.getSimpleName());
    }

    public <T> MongoCollection<T> getCollection(String collectionName,
            Class<T> documentClass) {
        return this.getMongoDatabase().getCollection(collectionName, documentClass);
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return this.getMongoDatabase().getCollection(collectionName);
    }

    public long count(Class<?> documentClass) {
        return this.getCollection(documentClass).count();
    }

    public GridFSBucket createBucket(String bucketName) {
        return GridFSBuckets.create(this.getMongoDatabase(), bucketName);
    }
}
