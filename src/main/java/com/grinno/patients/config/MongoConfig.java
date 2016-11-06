package com.grinno.patients.config;

import com.grinno.patients.model.Patient;
import com.grinno.patients.model.PersistentLogin;
import com.grinno.patients.model.User;
import org.bson.codecs.configuration.CodecRegistries;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import javax.annotation.PostConstruct;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig {

    @Bean
    public MongoClient mongoClient(MongoProperties properties) {
        MongoClientURI uri = new MongoClientURI(properties.getUri());
        return new MongoClient(uri);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient, MongoProperties properties) {
        MongoClientURI uri = new MongoClientURI(properties.getUri());
        return mongoClient.getDatabase(uri.getDatabase()).withCodecRegistry(CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(new ListCodec.Provider()),
                CodecRegistries.fromProviders(new PojoCodecProvider())));
    }

    @Bean
    public MongoDbFactory mongoDbFactory(MongoProperties properties) throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(properties.getUri()));
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoProperties properties) throws Exception {
        return new MongoTemplate(mongoDbFactory(properties));
    }

//    @PostConstruct
//    public void createCollections(MongoTemplate mongoTemplate) {
//        if (!mongoTemplate.collectionExists(User.class)) {
//            mongoTemplate.createCollection(User.class);
//        }
//        if (!mongoTemplate.collectionExists(PersistentLogin.class)) {
//            mongoTemplate.createCollection(PersistentLogin.class);
//        }
//        if (!mongoTemplate.collectionExists(Patient.class)) {
//            mongoTemplate.createCollection(Patient.class);
//        }
//    }
    
//    @PostConstruct
//    public void createIndexes() {
//        mongoTemplate.indexOps(User.class).ensureIndex(new Index("email", ASC));
//        mongoTemplate.indexOps(PersistentLogin.class).ensureIndex(new Index("userId", ASC));
//        mongoTemplate.indexOps(Patient.class).ensureIndex(new Index("pesel", ASC));
//        mongoTemplate.indexOps(Patient.class).ensureIndex(new CompoundIndexDefinition("pesel", ASC));
//    }
}
