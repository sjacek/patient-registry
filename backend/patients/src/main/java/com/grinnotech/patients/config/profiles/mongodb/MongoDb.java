package com.grinnotech.patients.config.profiles.mongodb;

//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//
//import org.bson.Document;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@Profile("mongodb")
//public class MongoDb {
//
//    private final MongoDatabase mongoDatabase;
//
//	@Contract(pure = true)
//	public MongoDb(MongoDatabase mongoDatabase) {
//		this.mongoDatabase = mongoDatabase;
//	}
//
//	@NotNull
//    private static String getCollectionName(@NotNull Class<?> documentClass) {
//        return StringUtils.uncapitalize(documentClass.getSimpleName());
//    }
//
//    @PostConstruct
//    public void createIndexes() {
////        String idxName = CUser.email + "_version";
////        if (!indexExists(User.class, idxName)) {
////            getCollection(User.class).createIndex(ascending(CUser.email, "_version"), new IndexOptions().name(idxName).unique(true));
////        }
////
////        idxName = CUser.email + "_chain_id";
////        if (!indexExists(User.class, idxName)) {
////            getCollection(User.class).createIndex(ascending(CUser.email, "_chain_id"), new IndexOptions().name(idxName));
////        }
//
////        if (!indexExists(PersistentLogin.class, CPersistentLogin.userId)) {
////            getCollection(PersistentLogin.class).createIndex(ascending(CPersistentLogin.userId));
////        }
//    }
//
//    private boolean indexExists(Class<?> clazz, String indexName) {
//        return indexExists(getCollection(clazz), indexName);
//    }
//
////    private MongoDatabase getMongoDatabase() {
////        return mongoDatabase;
////    }
//
//    private boolean indexExists(@NotNull MongoCollection<?> collection, String indexName) {
//        for (Document doc : collection.listIndexes()) {
//            if (doc.containsKey("key")) {
//                Document key = (Document) doc.get("key");
//
//                if (key.containsKey(indexName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public <T> MongoCollection<T> getCollection(Class<T> documentClass) {
//        return mongoDatabase.getCollection(getCollectionName(documentClass), documentClass);
//    }
//
//    public <T> MongoCollection<T> getCollection(String collectionName, Class<T> documentClass) {
//        return mongoDatabase.getCollection(collectionName, documentClass);
//    }
//
//    public MongoCollection<Document> getCollection(String collectionName) {
//        return mongoDatabase.getCollection(collectionName);
//    }
//}
