package com.grinno.patients.config;

import com.mongodb.Mongo;
import org.bson.codecs.configuration.CodecRegistries;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfig {

    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public MongoClient mongoClient(MongoProperties properties) {
        MongoClientURI uri = new MongoClientURI(properties.getUri());
        LOGGER.debug(uri.getURI());
        return new MongoClient(uri);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient, MongoProperties properties) {
        MongoClientURI uri = new MongoClientURI(properties.getUri());
        LOGGER.debug(uri.getURI());
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
}
