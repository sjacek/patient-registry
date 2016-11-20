package com.grinno.patients.config;

import org.bson.codecs.configuration.CodecRegistries;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
public class MongoConfig {

    @Value("${mongodb.uri}")
    private String mongodbUri;
    
    @Bean
    public MongoClient mongoClient() {
        MongoClientURI uri = new MongoClientURI(mongodbUri);
        return new MongoClient(uri);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient) {
        MongoClientURI uri = new MongoClientURI(mongodbUri);
        return mongoClient.getDatabase(uri.getDatabase()).withCodecRegistry(
                CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(new ListCodec.Provider()),
                CodecRegistries.fromProviders(new PojoCodecProvider())));
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClientURI(mongodbUri));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
